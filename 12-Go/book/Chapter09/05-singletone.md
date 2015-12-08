Singleton Pattern in Go
Jul 12, 2015
The Go Language growth has been phenomenal in the last few years, and is attracting language converts from all walks of life. There has been a lot articles recently about companies switching from Ruby, and experiencing the new world of Go and it’s parallel and concurrent approach to problems.

In the last 10 years, Ruby on Rails has allowed many developers and startups to quickly develop powerful systems, most of the time without needing to worry on how the inner things work, or worry about thread-safety and concurrency. It is very rare for a RoR application to create threads and run things in parallel. The whole hosting infrastructure and framework stack uses a different approach, by parallelizing it via multiple processes. It has only been in the last few years that multithreaded rack servers like Puma has surged in popularity, but even that brought a lot of issues in the beggining with third-party gems and other code that weren’t designed to be thread safe.

Now with a lot of new developers embarking into the Go Language boat, we need to carefully look at our code and see how it will behave, it needs to be designed in a thread-safe way.

The Common Mistake
Recently, I have seen this kind of mistake more and more in github repositories. Singleton implementations that doesn’t have any consideration for thread-safety. Below is the most common example of this mistake.
```go
package singleton

type singleton struct {
}

var instance *singleton

func GetInstance() *singleton {
	if instance == nil {
		instance = &singleton{}   // <--- NOT THREAD SAFE
	}
	return instance
}
```
In the above scenario, multiple go routines could evaluate the first check and they would all create an instance of the singleton type and override each other. There is no guarantee which instance it will be returned here, and other further operations on the instance can be come inconsistent with the expectations by the developer.

The reason this is bad is that if references to the singleton instance are being held around through the code, there could be potentially multiple instances of the type with different states, generating potential different code behaviours. It also becomes a real nightmare during debugging and becomes really hard to spot the the bug, since that at debugging time nothing really appears to be wrong due to the run-time pauses minimizing the potential of a non-thread-safe execution, easily hiding the problem from the developer.

The Aggressive Locking
I have also seen this poor solution to the thread-safety problem. Indeed this solves the thread-safety issue, but creates other potential serious problems. It introduces a threading contention by perform aggressive locking of the entire method.
```go
var mu Sync.Mutex

func GetInstance() *singleton {
    mu.Lock()                    // <--- Unnecessary locking if instance already created
    defer mu.Unlock()

    if instance == nil {
        instance = &singleton{}
    }
    return instance
}
```
In the code above, we can see that we solve the thread-safety issue by introducing the Sync.Mutex and acquiring the Lock before creating the singleton instance. The problem is that here we are performing excessive locking even when we wouldn’t be required to do so, in the case the instance has been already created and we should simply have returned the cached singleton instance. On a highly concurrent code base, this can generate a bottle-neck since only one go routine could get the singleton instance at a time.

So, this is not the best approach. We have to look at other solutions.

Check-Lock-Check Pattern
In C++ and other languages, the best and safest way to ensure minimal locking and still be thread-safe is to utilize the well known pattern called Check-Lock-Check, when acquiring locks. The pseudo-code for the pattern is something like this.
```go
if check() {
    lock() {
        if check() {
            // perform your lock-safe code here
        }
    }
}
```
The idea behind this pattern is that you would want to check first, to minimize any aggressive locking, since an IF statement is less expensive than the locking. Secondly we would want to wait and acquire the exclusive lock so only one execution is inside that block at a single time. But butween the first check and the acquisition of the exclusive lock there could have been another thread that did acquire the lock, therefore we would need to check again inside the lock to avoid replacing the instance with another one.

Over the years, the people that has worked with me knows this well, that I have been very strict with my engineering teams during code-reviews about this pattern and thread-safety mentality.

If we apply this pattern to our GetInstance() method we would have something as follow:
```go
func GetInstance() *singleton {
    if instance == nil {     // <-- Not yet perfect. since it's not fully atomic
        mu.Lock()
        defer mu.Unlock()

        if instance == nil {
            instance = &singleton{}
        }
    }
    return instance
}
```
This is a better approach, but still is not perfect. Since due to compiler optimizations there is not an atomic check on the instance store state. With all the technical considerations this is still not perfect. But it is much better than the initial approach.

But using the sync/atomic package, we can atomically load and set a flag that will indicate if we have initialized or not our instance.
```go
import "sync"
import "sync/atomic"

var initialized uint32
...

func GetInstance() *singleton {

    if atomic.LoadUInt32(&initialized) == 1 {
		return instance
	}

    mu.Lock()
    defer mu.Unlock()

    if initialized == 0 {
         instance = &singleton{}
         atomic.StoreUint32(&initialized, 1)
    }

    return instance
}
```
But… I believe we could do better by looking into how the Go Language and standard library implements go routines synchronization.

An Idiomatic Singleton Approach in Go
We want to implement this Singleton pattern utilizing the Go idiomatic way of doing things. So we have to look at the excellent standard library packaged called sync. We can find the type Once. This object will perform an action exactly once and no more. Below you can find the source code from the Go standard library.
```go
// Once is an object that will perform exactly one action.
type Once struct {
	m    Mutex
	done uint32
}

// Do calls the function f if and only if Do is being called for the
// first time for this instance of Once. In other words, given
// 	var once Once
// if once.Do(f) is called multiple times, only the first call will invoke f,
// even if f has a different value in each invocation.  A new instance of
// Once is required for each function to execute.
//
// Do is intended for initialization that must be run exactly once.  Since f
// is niladic, it may be necessary to use a function literal to capture the
// arguments to a function to be invoked by Do:
// 	config.once.Do(func() { config.init(filename) })
//
// Because no call to Do returns until the one call to f returns, if f causes
// Do to be called, it will deadlock.
//
// If f panics, Do considers it to have returned; future calls of Do return
// without calling f.
//
func (o *Once) Do(f func()) {
	if atomic.LoadUint32(&o.done) == 1 { // <-- Check
		return
	}
	// Slow-path.
	o.m.Lock()                           // <-- Lock
	defer o.m.Unlock()
	if o.done == 0 {                     // <-- Check
		defer atomic.StoreUint32(&o.done, 1)
		f()
	}
}
```
What this means is the we can leverage the awesome Go sync package to invoke a method exactly only once. Therefore, we can invoke the once.Do() method like this:
```go
once.Do(func() {
    // perform safe initialization here
})
```
Below you can find the full code of this singleton implementation that utilizes the sync.Once type to syncronize access to the GetInstance() and ensures that our type only gets initialized exactly once.
```go
package singleton

import (
    "sync"
)

type singleton struct {
}

var instance *singleton
var once sync.Once

func GetInstance() *singleton {
    once.Do(func() {
        instance = &singleton{}
    })
    return instance
}
```
Therefore, using the sync.Once package is the preferred way of implementing this safely, in similar way that Objective-C and Swift (Cocoa) implements the dispatch_once metod to perform similar initialization.

Conclusion
When it comes to concurrent and parallel code, a lot more careful examination of your code is needed. Always have your team members perform code-reviews, since things like this is easy to have an oversight.

All the new developers that are switching to Go needs to really understand how thread-safety works to better improve their code. Even though the Go language itself does a lot of the heavy-lifting by allowing you to design concurrent code with minimal knowledge of concurrency. There are several cases where the language doesn’t help you, and you still need to apply best practices in developing your code.
