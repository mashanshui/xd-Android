package cn.edu.ahnu.wjcy.myapplication.Model;

import java.util.LinkedList;

/**
 * Created by Cuibiming on 2017-10-12.
 */

public class OrderQueue {
    private LinkedList<PreOrder> list;

    public OrderQueue() {
        list = new LinkedList<>();
    }

    /**
     * 销毁队列
     */
    public void clear()
    {
        list.clear();
    }

    /**
     * 判断队列是否为空
     * @return
     */
    public boolean QueueEmpty()
    {
        return list.isEmpty();
    }

    /**
     * 进队
     * @param o
     */
    public void enQueue(PreOrder o)
    {
        list.addLast(o);
    }

    /**
     * 出队
     * @return
     */
    public PreOrder deQueue()
    {
        if(!list.isEmpty())
        {
            return list.removeFirst();
        }
        return null;
    }

    /**
     * 获取队列长度
     * @return
     */
    public int QueueLength()
    {
        return list.size();
    }

    /**
     * 查看队首元素
     * @return
     */
    public Object QueuePeek()
    {
        return list.getFirst();
    }
}
