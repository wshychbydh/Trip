package cooleye.service.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Subscriber;

public class SubscriberMapper {
    //// FIXME: 16/6/21
    private static SubscriberMapper sInstance = new SubscriberMapper();
    
    private final Map<Object, ArrayList<Subscriber>> mSubMapper = new HashMap<Object, ArrayList<Subscriber>>();
    
    public static SubscriberMapper getInstance() {
        return sInstance;
    }
    
    private SubscriberMapper() {
    }
    
    public void add(Object tag, Subscriber request) {
        ArrayList<Subscriber> queue = sInstance.mSubMapper.get(tag);
        if (queue != null) {
            queue.add(request);
        } else {
            ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
            subscribers.add(request);
            sInstance.mSubMapper.put(tag, subscribers);
        }
    }
    
    public void cancelAll(Object tag) {
        if (tag == null) {
            return;
        }
        ArrayList<Subscriber> subscribers = sInstance.mSubMapper.get(tag);
        if (subscribers != null && !subscribers.isEmpty()) {
            for (Subscriber subscriber : subscribers) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.unsubscribe();
                }
            }
        }
        sInstance.mSubMapper.remove(tag);
        
    }

    /**
     * Iterator.remove()方法保证modCount和expectedModCount的值的一致性，避免抛出ConcurrentModificationException异常。
     *
     * @param tag
     */
    public void remove(Subscriber tag) {
        if (tag == null) {
            return;
        }
        if (sInstance.mSubMapper != null && !sInstance.mSubMapper.isEmpty()) {
            Iterator<Map.Entry<Object, ArrayList<Subscriber>>> iterator = sInstance.mSubMapper.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, ArrayList<Subscriber>> next = iterator.next();
                ArrayList<Subscriber> value = next.getValue();
                if (value != null && !value.isEmpty()) {
                    Iterator<Subscriber> subs = value.iterator();
                    while (subs.hasNext()) {
                        Subscriber subscriber = subs.next();
                        if (tag.equals(subscriber)) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.unsubscribe();
                            }
                            subs.remove();
                        }
                    }
                    if (subs.hasNext()) {
                        iterator.remove();
                    }

                }

            }

        }
    }
}
