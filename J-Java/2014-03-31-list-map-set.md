
List与Set与Map详解
=========

@(J-Java)[HashMap|ArrayList|HashTable|LinkedList]

###List，Set，Map的类层次关系：
    
    Collection
        |____List
            |____LinkedList
            |____ArrayList
            |____Vector
                |____Stack
            |____Set
    
    Map
        |____Hashtable
        |____HashMap
        |____WeakHashMap

###Collection接口
Collection是最基本的集合接口。有些Collection允许有相同的元素，有些不行。有些支持排序，有些不支持。Java SDK不提供直接继承自Collection的类，Java SDK提供的类都是继承自Collection的“子接口”如List和Set。
所有实现Collection接口的类都必须提供两个标准的构造函数：
- 无参数的构造函数用于创建一个空的Collection。
- 有一个 Collection参数的构造函数用于创建一个新的Collection，这个新的Collection与传入的Collection有相同的元素。后一个构造函数允许用户复制一个Collection。

如何遍历Collection中的每一个元素？不论Collection的实际类型如何，它都支持一个iterator()的方法，该方法返回一个迭代子，使用该迭代子即可逐一访问Collection中每一个元素。典型的用法如下：

    Iterator it = collection.iterator(); // 获得一个迭代子
    while (it.hasNext()) {  
        Object obj = it.next(); // 得到下一个元素  
    }  

由Collection接口派生的两个接口是List和Set。
###List接口
List是有序的Collection，使用此接口能够精确的控制每个元素插入的位置。用户能够使用索引（类似于数组下标）来访问List中的元素。`与Set不同，List允许有相同的元素`。除了具有Collection接口必备的iterator()方法外，List还提供一个listIterator()方法，返回一个ListIterator接口，和标准的Iterator接口相比，ListIterator多了一些add()之类的方法，允许添加，删除，设定元素，还能向前或向后遍历。实现List接口的常用类有LinkedList，ArrayList，Vector和Stack。
    
    1. LinkedList类
    LinkedList实现了List接口，允许null元素。此外LinkedList提供额外的get，remove，insert方法在 LinkedList的首部或尾部。这些操作使LinkedList可被用作堆栈（stack），队列（queue）或双向队列（deque）。
    注意LinkedList没有同步方法。如果多个线程同时访问一个List，则必须自己实现访问同步。一种解决方法是在创建List时构造一个同步的List：
    List list = Collections.synchronizedList(new LinkedList(...));
    2. ArrayList类
    ArrayList实现了可变大小的数组。它允许所有元素，包括null。ArrayList没有同步。
    size，isEmpty，get，set方法运行时间为常数。但是add方法开销为分摊的常数，添加n个元素需要O(n)的时间。其他的方法运行时间为线性。
    每个ArrayList实例都有一个容量（Capacity），即用于存储元素的数组的大小。这个容量可随着不断添加新元素而自动增加，但是增长算法并没有定义。当需要插入大量元素时，在插入前可以调用ensureCapacity方法来增加ArrayList的容量以提高插入效率。
    和LinkedList一样，ArrayList也是非同步的（unsynchronized）。
    3. Vector类
    Vector非常类似ArrayList，但是Vector是同步的。由Vector创建的Iterator，虽然和ArrayList创建的 Iterator是同一接口，但是，因为Vector是同步的，当一个Iterator被创建而且正在被使用，另一个线程改变了Vector的状态（例如，添加或删除了一些元素），这时调用Iterator的方法时将抛出ConcurrentModificationException，因此必须捕获该异常。
    4. Stack 类
    Stack继承自Vector，实现一个后进先出的堆栈。Stack提供5个额外的方法使得Vector得以被当作堆栈使用。基本的push和pop 方法，还有peek方法得到栈顶的元素，empty方法测试堆栈是否为空，search方法检测一个元素在堆栈中的位置。Stack刚创建后是空栈。

###Set接口
`Set是一种不包含重复的元素的Collection`，即任意的两个元素e1和e2都有e1.equals(e2)=false，Set最多有一个null元素。
很明显，Set的构造函数有一个约束条件，传入的Collection参数不能包含重复的元素。
请注意：必须小心操作可变对象（Mutable Object）。如果一个Set中的可变元素改变了自身状态导致Object.equals(Object)=true将导致问题。

###Map接口
注意，Map没有继承Collection接口，Map提供key到value的映射。一个Map中不能包含相同的key，每个key只能映射一个 value。

Map接口提供3种集合的视图，Map的内容可以被当作一组key集合，一组value集合，或者一组key-value映射。

    put(Object key, Object value)添加一个“值”(想要得东西)和与“值”相关联的“键”(key)(使用它来查找)。
    get(Object key)返回与给定“键”相关联的“值”。可以用containsKey()和containsValue()测试Map中是否包含某个“键”或“值”。

标准的Java类库中包含了几种不同的Map：HashMap, TreeMap, LinkedHashMap, WeakHashMap, IdentityHashMap。它们都有同样的基本接口Map，但是行为、效率、排序策略、保存对象的生命周期和判定“键”等价的策略等各不相同。
执行效率是Map的一个大问题。看看get()要做哪些事，就会明白为什么在ArrayList中搜索“键”是相当慢的。而这正是HashMap提高速度的地方。HashMap使用了特殊的值，称为“散列码”(hash code)，来取代对键的缓慢搜索。“散列码”是“相对唯一”用以代表对象的int值，它是通过将该对象的某些信息进行转换而生成的。所有Java对象都能产生散列码，因为hashCode()是定义在基类Object中的方法。
###Hashtable类
Hashtable继承自Map接口，实现一个key-value映射的哈希表。任何非空（non-null）的对象都可作为key或者value。
添加数据使用put(key, value)，取出数据使用get(key)，这两个基本操作的时间开销为常数。
Hashtable通过initial capacity和load factor两个参数调整性能。通常缺省的load factor 0.75较好地实现了时间和空间的均衡。增大load factor可以节省空间但相应的查找时间将增大，这会影响像get和put这样的操作。

    使用Hashtable的简单示例如下，将1，2，3放到Hashtable中，他们的key分别是”one”，”two”，”three”：
    Hashtable numbers = new Hashtable();  
    numbers.put(“one”, new Integer(1));  
    numbers.put(“two”, new Integer(2));  
    numbers.put(“three”, new Integer(3));  

    要取出一个数，比如2，用相应的key：
    Integer n = (Integer)numbers.get(“two”);  
    System.out.println(“two = ” + n);  

由于作为key的对象将通过计算其散列函数来确定与之对应的value的位置，因此任何作为key的对象都必须实现hashCode和equals方法。hashCode和equals方法继承自根类Object，如果你用自定义的类当作key的话，要相当小心，按照散列函数的定义，如果两个对象相同，即obj1.equals(obj2)=true，则它们的hashCode必须相同，但如果两个对象不同，则它们的hashCode不一定不同，如果两个不同对象的hashCode相同，这种现象称为冲突，冲突会导致操作哈希表的时间开销增大，所以尽量定义好的hashCode()方法，能加快哈希表的操作。
如果相同的对象有不同的hashCode，对哈希表的操作会出现意想不到的结果（期待的get方法返回null），要避免这种问题，只需要牢记一条：要同时复写equals方法和hashCode方法，而不要只写其中一个。
Hashtable是同步的。
###HashMap类
HashMap和Hashtable类似，不同之处在于HashMap是非同步的，并且允许null，即null key和null value。

但是将HashMap视为Collection时（values()方法可返回Collection），其迭代子操作时间开销和HashMap 的容量成比例。

因此，如果迭代操作的性能相当重要的话，不要将HashMap的初始化容量设得过高，或者load factor过低。

###WeakHashMap类
WeakHashMap是一种改进的HashMap，它对key实行“弱引用”，如果一个key不再被外部所引用，那么该key可以被GC回收。

###总结
如果涉及到堆栈，队列等操作，应该考虑用List，对于需要快速插入，删除元素，应该使用LinkedList，如果需要快速随机访问元素，应该使用ArrayList。
如果程序在单线程环境中，或者访问仅仅在一个线程中进行，考虑非同步的类，其效率较高，如果多个线程可能同时操作一个类，应该使用同步的类。
要特别注意对哈希表的操作，作为key的对象要正确复写equals和hashCode方法。
尽量返回接口而非实际的类型，如返回List而非ArrayList，这样如果以后需要将ArrayList换成LinkedList时，客户端代码不用改变。这就是针对抽象编程。

###ArrayList与Vector区别

- 同步性

Vector是同步的。这个类中的一些方法保证了Vector中的对象是线程安全的。而ArrayList则是异步的，因此ArrayList中的对象并不是线程安全的。因为同步的要求会影响执行的效率，所以如果你不需要线程安全的集合那么使用ArrayList是一个很好的选择，这样可以避免由于同步带来的不必要的性能开销。 

- 数据增长 

从内部实现机制来讲ArrayList和Vector都是使用数组(Array)来控制集合中的对象。当你向这两种类型中增加元素的时候，如果元素的数目超出了内部数组目前的长度它们都需要扩展内部数组的长度，Vector缺省情况下自动增长原来一倍的数组长度，ArrayList是原来的50%,所以最后你获得的这个集合所占的空间总是比你实际需要的要大。所以如果你要在集合中保存大量的数据那么使用Vector有一些优势，因为你可以通过设置集合的初始化大小来避免不必要的资源开销。 

- 使用模式

在ArrayList和Vector中，从一个指定的位置（通过索引）查找数据或是在集合的末尾增加、移除一个元素所花费的时间是一样的，这个时间我们用O(1)表示。但是，如果在集合的其他位置增加或移除元素那么花费的时间会呈线形增长：O(n-i)，其中n代表集合中元素的个数，i代表元素增加或移除元素的索引位置。为什么会这样呢？以为在进行上述操作的时候集合中第i和第i个元素之后的所有元素都要执行位移的操作。这一切意味着什么呢？ 
这意味着，你只是查找特定位置的元素或只在集合的末端增加、移除元素，那么使用Vector或ArrayList都可以。如果是其他操作，你最好选择其他的集合操作类。比如，LinkList集合类在增加或移除集合中任何位置的元素所花费的时间都是一样的?O(1)，但它在索引一个元素的使用缺比较慢－O(i),其中i是索引的位置.使用ArrayList也很容易，因为你可以简单的使用索引来代替创建iterator对象的操作。LinkList也会为每个插入的元素创建对象，所有你要明白它也会带来额外的开销。 

最后，在《Practical Java》一书中Peter Haggar建议使用一个简单的数组（Array）来代替Vector或ArrayList。尤其是对于执行效率要求高的程序更应如此。因为使用数组(Array)避免了同步、额外的方法调用和不必要的重新分配空间的操作。

###HashMap与HashTable区别
HashMap是新框架中用来代替HashTable的类，也就是说建议使用HashMap，不要使用HashTable。可能你觉得HashTable很好用，为什么不用呢？这里简单分析他们的区别。 

    1.HashTable的方法是同步的，HashMap未经同步，所以在多线程场合要手动同步HashMap这个区别就像Vector和ArrayList一样。
    2.HashTable不允许null值(key和value都不可以),HashMap允许null值(key和value都可以)。
    3.HashTable有一个contains(Object value)，功能和containsValue(Object value)功能一样。
    4.HashTable使用Enumeration，HashMap使用Iterator。
    以上只是表面的不同，它们的实现也有很大的不同。
    
    5.HashTable中hash数组默认大小是11，增加的方式是 old*2+1。HashMap中hash数组的默认大小是16，而且一定是2的指数。
    6.哈希值的使用不同：
    6.1）HashTable直接使用对象的hashCode，代码是这样的：
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
    6.2）而HashMap重新计算hash值，而且用与代替求模：
    int hash = hash(k);
    int i = indexFor(hash, table.length);
    
    static int hash(Object x) {
    　　int h = x.hashCode();
    
    　　h += ~(h << 9);
    　　h ^= (h >>> 14);
    　　h += (h << 4);
    　　h ^= (h >>> 10);
    　　return h;
    }
    static int indexFor(int h, int length) {
    　　return h & (length-1);
    }
    以上只是一些比较突出的区别，当然他们的实现上还是有很多不同的，比如HashMap对null的操作


