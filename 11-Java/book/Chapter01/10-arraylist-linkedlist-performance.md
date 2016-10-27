ArrayList和LinkedList的几种循环遍历方式及性能对比分析
===

### 1. List的五种遍历方式
下面只是简单介绍各种遍历示例(以ArrayList为例)，各自优劣会在本文后面进行分析给出结论。

(1) for each循环

```java
List<Integer> list = new ArrayList<Integer>();
for (Integer j : list) {
    // use j
}
```

(2) 显示调用集合迭代器

```java
List<Integer> list = new ArrayList<Integer>();
for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
    iterator.next();
}

或

List<Integer> list = new ArrayList<Integer>();
Iterator<Integer> iterator = list.iterator();
while (iterator.hasNext()) {
    iterator.next();
}
```

(3) 下标递增循环，终止条件为每次调用size()函数比较判断

```ava
List<Integer> list = new ArrayList<Integer>();
for (int j = 0; j < list.size(); j++) {
    list.get(j);
}
```

(4) 下标递增循环，终止条件为和等于size()的临时变量比较判断

```java
List<Integer> list = new ArrayList<Integer>();
int size = list.size();
for (int j = 0; j < size; j++) {
    list.get(j);
}
```

(5) 下标递减循环

```java
List<Integer> list = new ArrayList<Integer>();
for (int j = list.size() - 1; j >= 0; j--) {
    list.get(j);
}
```

### 2、List五种遍历方式的性能测试及对比



PS：如果运行报异常in thread “main” java.lang.OutOfMemoryError: Java heap space，请将main函数里面list size的大小减小。

测试环境为Windows7 32位系统 3.2G双核CPU 4G内存，Java 7，Eclipse -Xms512m -Xmx512m
最终测试结果如下：

```
compare loop performance of ArrayList
-----------------------------------------------------------------------
list size              | 10,000    | 100,000   | 1,000,000 | 10,000,000
-----------------------------------------------------------------------
for each               | 1 ms      | 3 ms      | 14 ms     | 152 ms    
-----------------------------------------------------------------------
for iterator           | 0 ms      | 1 ms      | 12 ms     | 114 ms    
-----------------------------------------------------------------------
for list.size()        | 1 ms      | 1 ms      | 13 ms     | 128 ms    
-----------------------------------------------------------------------
for size = list.size() | 0 ms      | 0 ms      | 6 ms      | 62 ms     
-----------------------------------------------------------------------
for j--                | 0 ms      | 1 ms      | 6 ms      | 63 ms     
-----------------------------------------------------------------------

compare loop performance of LinkedList
-----------------------------------------------------------------------
list size              | 100       | 1,000     | 10,000    | 100,000   
-----------------------------------------------------------------------
for each               | 0 ms      | 1 ms      | 1 ms      | 2 ms      
-----------------------------------------------------------------------
for iterator           | 0 ms      | 0 ms      | 0 ms      | 2 ms      
-----------------------------------------------------------------------
for list.size()        | 0 ms      | 1 ms      | 73 ms     | 7972 ms   
-----------------------------------------------------------------------
for size = list.size() | 0 ms      | 0 ms      | 67 ms     | 8216 ms   
-----------------------------------------------------------------------
for j--                | 0 ms      | 1 ms      | 67 ms     | 8277 ms   
-----------------------------------------------------------------------
```

第一张表为ArrayList对比结果，第二张表为LinkedList对比结果。

PS：由于首次遍历List会稍微多耗时一点，for each的结果稍微有点偏差，将测试代码中的几个Type顺序调换会发现，for each耗时和for iterator接近。

### 3、遍历方式性能测试结果分析

(1) foreach介绍

```
foreach是Java SE5.0引入的功能很强的循环结构，for (Integer j : list)应读作for each int in list。
for (Integer j : list)实现几乎等价于

Iterator<Integer> iterator = list.iterator();
while(iterator.hasNext()) {
    Integer j = iterator.next();
}
```

下面的分析会将foreach和显示调用集合迭代器两种遍历方式归类为Iterator方式，其他三种称为get方式遍历。
这时我们已经发现foreach的一大好处，简单一行实现了四行的功能，使得代码简洁美观，另一大好处是相对于下标循环而言的，foreach不必关心下标初始值和终止值及越界等，所以不易出错。Effective-Java中推荐使用此种写法遍历，本文会验证这个说法。

使用foreach结构的类对象必须实现了Iterable接口，Java的Collection继承自此接口，List实现了Collection，这个接口仅包含一个函数，源码如下：

```java
package java.lang;

import java.util.Iterator;

/**
 * Implementing this interface allows an object to be the target of
 * the "foreach" statement.
 *
 * @param <T> the type of elements returned by the iterator
 *
 * @since 1.5
 */
public interface Iterable<T> {

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an Iterator.
     */
    Iterator<T> iterator();
}
```

iterator()用于返回一个Iterator，从foreach的等价实现中我们可以看到，会调用这个函数得到Iterator，再通过Iterator的next()得到下一个元素，hasNext()判断是否还有更多元素。Iterator源码如下：

```java
public interface Iterator<E> {
    boolean hasNext();

    E next();

    void remove();
}
```

(2) ArrayList遍历方式结果分析

```
compare loop performance of ArrayList
-----------------------------------------------------------------------
list size              | 10,000    | 100,000   | 1,000,000 | 10,000,000
-----------------------------------------------------------------------
for each               | 1 ms      | 3 ms      | 14 ms     | 152 ms    
-----------------------------------------------------------------------
for iterator           | 0 ms      | 1 ms      | 12 ms     | 114 ms    
-----------------------------------------------------------------------
for list.size()        | 1 ms      | 1 ms      | 13 ms     | 128 ms    
-----------------------------------------------------------------------
for size = list.size() | 0 ms      | 0 ms      | 6 ms      | 62 ms     
-----------------------------------------------------------------------
for j--                | 0 ms      | 1 ms      | 6 ms      | 63 ms     
-----------------------------------------------------------------------
```

PS：由于首次遍历List会稍微多耗时一点，for each的结果稍微有点偏差，将测试代码中的几个Type顺序调换会发现，for each耗时和for iterator接近。
从上面我们可以看出：

1. 在ArrayList大小为十万之前，五种遍历方式时间消耗几乎一样
2. 在十万以后，第四、五种遍历方式快于前三种，get方式优于Iterator方式，并且

```java
int size = list.size();
for (int j = 0; j < size; j++) {
    list.get(j);
}
```

用临时变量size取代list.size()性能更优。我们看看ArrayList中迭代器Iterator和get方法的实现

```java
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    public boolean hasNext() {
        return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length)
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }
    ……
}

public E get(int index) {
    rangeCheck(index);

    return elementData(index);
}
```

从中可以看出get和Iterator的next函数同样通过直接定位数据获取元素，只是多了几个判断而已。
c . 从上可以看出即便在千万大小的ArrayList中，几种遍历方式相差也不过50ms左右，且在常用的十万左右时间几乎相等，考虑foreach的优点，我们大可选用foreach这种简便方式进行遍历。

(3) LinkedList遍历方式结果分析

```
compare loop performance of LinkedList
-----------------------------------------------------------------------
list size              | 100       | 1,000     | 10,000    | 100,000   
-----------------------------------------------------------------------
for each               | 0 ms      | 1 ms      | 1 ms      | 2 ms      
-----------------------------------------------------------------------
for iterator           | 0 ms      | 0 ms      | 0 ms      | 2 ms      
-----------------------------------------------------------------------
for list.size()        | 0 ms      | 1 ms      | 73 ms     | 7972 ms   
-----------------------------------------------------------------------
for size = list.size() | 0 ms      | 0 ms      | 67 ms     | 8216 ms   
-----------------------------------------------------------------------
for j--                | 0 ms      | 1 ms      | 67 ms     | 8277 ms   
-----------------------------------------------------------------------
```

PS：由于首次遍历List会稍微多耗时一点，for each的结果稍微有点偏差，将测试代码中的几个Type顺序调换会发现，for each耗时和for iterator接近。
从上面我们可以看出：
在LinkedList大小接近一万时，get方式和Iterator方式就已经差了差不多两个数量级，十万时Iterator方式性能已经远胜于get方式。
我们看看LinkedList中迭代器和get方法的实现

```java
private class ListItr implements ListIterator<E> {
    private Node<E> lastReturned = null;
    private Node<E> next;
    private int nextIndex;
    private int expectedModCount = modCount;

    ListItr(int index) {
        // assert isPositionIndex(index);
        next = (index == size) ? null : node(index);
        nextIndex = index;
    }

    public boolean hasNext() {
        return nextIndex < size;
    }

    public E next() {
        checkForComodification();
        if (!hasNext())
            throw new NoSuchElementException();

        lastReturned = next;
        next = next.next;
        nextIndex++;
        return lastReturned.item;
    }
    ……
}

public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
}

/**
 * Returns the (non-null) Node at the specified element index.
 */
Node<E> node(int index) {
    // assert isElementIndex(index);

    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
}
```

从上面代码中可以看出LinkedList迭代器的next函数只是通过next指针快速得到下一个元素并返回。而get方法会从头遍历直到index下标，查找一个元素时间复杂度为哦O(n)，遍历的时间复杂度就达到了O(n2)。
所以对于LinkedList的遍历推荐使用foreach，避免使用get方式遍历。

(4) ArrayList和LinkedList遍历方式结果对比分析

从上面的数量级来看，同样是foreach循环遍历，ArrayList和LinkedList时间差不多，可将本例稍作修改加大list size会发现两者基本在一个数量级上。
但ArrayList get函数直接定位获取的方式时间复杂度为O(1)，而LinkedList的get函数时间复杂度为O(n)。
再结合考虑空间消耗的话，建议首选ArrayList。对于个别插入删除非常多的可以使用LinkedList。

### 4、结论总结

通过上面的分析我们基本可以总结下：
1. 无论ArrayList还是LinkedList，遍历建议使用foreach，尤其是数据量较大时LinkedList避免使用get遍历。
2. List使用首选ArrayList。对于个别插入删除非常多的可以使用LinkedList。
3. 可能在遍历List循环内部需要使用到下标，这时综合考虑下是使用foreach和自增count还是get方式。
