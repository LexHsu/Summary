
List, Set, Map
=========

### List，Set，Map 的类层次关系：

```java
Collection
    |---- List
        |---- LinkedList
        |---- ArrayList
        |---- Vector
            |---- Stack
        |---- Set

Map
    |---- Hashtable
    |---- HashMap
    |---- WeakHashMap
```

### 遍历 Collection

```java
Iterator it = collection.iterator(); // 获得一个迭代子
while (it.hasNext()) {
    Object obj = it.next();          // 获取下一个元素
}
```

### List

List 是有序的 Collection。与 Set 不同，List 允许有相同的元素。

```
LinkedList
1. 可用作堆栈（stack），队列（queue）或双向队列（deque）
2. 允许 null 元素
3. 无同步方法。多线程同时访问需同步，如：List list = Collections.synchronizedList(new LinkedList());

ArrayList
1. 可变大小的数组
2. 允许 null 元素
3. 无同步方法
4. 需要插入大量元素时，在插入前可以调用 ensureCapacity 方法来增加 ArrayList 的容量以提高插入效率

Vector
1. 线程安全的ArrayList
2. Vector默认自动增长为原来一倍，ArrayList是原来的一半

Stack
1. 继承自Vector的堆栈实现
```

### Set

Set 不包含重复元素，因此最多有一个 null 元素。


### Map

Map 提供 key 到 value 的映射。一个 Map 中不能包含相同的 key，每个 key 只能映射一个 value。

##### Hashtable, HashMap

```
1. HashTable 的方法线程安全，HashMap不是。
2. HashTable 不允许 null 值（key，value 均不可），HashMap 允许 null 值（key，value 均可）。
4. HashTable 使用 Enumeration，HashMap 使用 Iterator。
5. HashTable 中 hash 数组默认大小是 11，增加方式是 old * 2 + 1。
   HashMap中默认大小是16，增加方式是2的指数。
6. 哈希值的使用不同。
   1) HashTable直接使用对象的hashCode：
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    
   2) HashMap重新计算hash值，用与代替求模：
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
```

注意：作为 key 的对象需同时覆写 Object 的 hashCode 和 equals 方法，
如果相同的对象有不同的 hashCode，对哈希表的操作会出现意想不到的结果（期待的get方法返回null）。

散列函数定义：

- 如果两个对象相同，obj1.equals(obj2)为真，它们的hashCode相同
- 如果两个对象不同，它们的hashCode有可能相同。如果相同，称为冲突，冲突会导致操作哈希表的时间开销增大


##### WeakHashMap

改进的 HashMap，对 key 实行“弱引用”，如果一个 key 不再被外部引用，该 key 可被 GC 回收。

### 总结

在 ArrayList 和 Vector 中，从指定位置查找数据或是在集合末尾增加、移除一个元素所花费的时间复杂度都是 O(1)。
但如果在集合的其他位置增加或移除元素，复杂度程线性增：O(n - i)，
其中 n 代表集合中元素的个数，i 代表元素增加或移除元素的索引位置。
因为第 i 个元素及其之后的所有元素都要执行位移操作。

LinkList 集合类在增加或移除集合中任何位置的元素的时间复杂度都为O(1)，但索引元素较慢。

因此，涉及到堆栈，队列等操作，应该考虑用List，对于需要快速插入，删除元素，应该使用LinkedList，如果需要快速随机访问元素，应该使用ArrayList。

而对于执行效率要求高的程序，也可用数组代替Vector或ArrayList。因为使用数组避免了同步、额外的方法调用和不必要的重新分配空间的操作。
