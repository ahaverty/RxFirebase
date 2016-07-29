package com.ahaverty.rxfirebaseserver;

import com.ahaverty.rxfirebaseserver.Exceptions.RxFirebaseDataCastException;
import com.ahaverty.rxfirebaseserver.Exceptions.RxFirebaseDataException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public class RxFirebaseDatabase {

    public static <T> Observable<T> observeSingleValue(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        T value = dataSnapshot.getValue(clazz);
                        if (value != null) {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onNext(value);
                            }
                        } else {
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                            }
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });
            }
        });
    }

    public static <T> Observable<T> observeValue(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                final ValueEventListener valueEventListener = query.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                T value = dataSnapshot.getValue(clazz);
                                if (value != null) {
                                    if (!subscriber.isUnsubscribed()) {
                                        subscriber.onNext(value);
                                    }
                                } else {
                                    if (!subscriber.isUnsubscribed()) {
                                        subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataException(error));
                                }
                            }
                        });

                subscriber.add(Subscriptions.create(() -> query.removeEventListener(valueEventListener)));
            }
        });
    }


    public static <T> Observable<T> observeValues(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                final ValueEventListener valueEventListener = query.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    T value = childSnapshot.getValue(clazz);
                                    if (value == null) {
                                        if (!subscriber.isUnsubscribed()) {
                                            subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                        }
                                    } else {
                                        if (!subscriber.isUnsubscribed()) {
                                            subscriber.onNext(value);
                                        }
                                    }
                                }
                                if (!subscriber.isUnsubscribed()) {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataException(error));
                                }
                            }
                        });

                subscriber.add(Subscriptions.create(() -> query.removeEventListener(valueEventListener)));
            }

        });
    }


    public static <T> Observable<LinkedList<T>> observeValuesList(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<LinkedList<T>>() {
            @Override
            public void call(final Subscriber<? super LinkedList<T>> subscriber) {
                final ValueEventListener valueEventListener = query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LinkedList<T> items = new LinkedList<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            T value = childSnapshot.getValue(clazz);
                            if (value == null) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                }
                            } else {
                                items.add(value);
                            }
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(items);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });
                subscriber.add(Subscriptions.create(() -> query.removeEventListener(valueEventListener)));
            }

        });
    }


    public static <T> Observable<LinkedHashMap<String, T>> observeValuesMap(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<LinkedHashMap<String, T>>() {
            @Override
            public void call(final Subscriber<? super LinkedHashMap<String, T>> subscriber) {
                final ValueEventListener valueEventListener = query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LinkedHashMap<String, T> items = new LinkedHashMap<>();
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            T value = childSnapshot.getValue(clazz);
                            if (value == null) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataCastException("unable to cast firebase data response to " + clazz.getSimpleName()));
                                }
                            } else {
                                items.put(childSnapshot.getKey(), value);
                            }
                        }

                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(items);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new RxFirebaseDataException(error));
                        }
                    }
                });
                subscriber.add(Subscriptions.create(() -> query.removeEventListener(valueEventListener)));
            }

        });
    }


    public static <T> Observable<RxFirebaseChildEvent<T>> observeChildrenEvents(final Query query, final Class<T> clazz) {
        return Observable.create(new Observable.OnSubscribe<RxFirebaseChildEvent<T>>() {
            @Override
            public void call(final Subscriber<? super RxFirebaseChildEvent<T>> subscriber) {
                final ChildEventListener childEventListener =
                        query.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<>(dataSnapshot.getValue(clazz),
                                                    dataSnapshot.getKey(),
                                                    previousChildName,
                                                    RxFirebaseChildEvent.EventType.ADDED));
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<>(dataSnapshot.getValue(clazz),
                                                    dataSnapshot.getKey(),
                                                    previousChildName,
                                                    RxFirebaseChildEvent.EventType.CHANGED));
                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<>(dataSnapshot.getValue(clazz),
                                                    dataSnapshot.getKey(),
                                                    RxFirebaseChildEvent.EventType.REMOVED));
                                }
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(
                                            new RxFirebaseChildEvent<>(dataSnapshot.getValue(clazz),
                                                    dataSnapshot.getKey(),
                                                    previousChildName,
                                                    RxFirebaseChildEvent.EventType.MOVED));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onError(new RxFirebaseDataException(error));
                                }
                            }
                        });

                subscriber.add(Subscriptions.create(() -> query.removeEventListener(childEventListener)));
            }
        });
    }

}
