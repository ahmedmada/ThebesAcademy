package com.eaapps.thebesacademy.Model;

import android.content.Context;

import com.eaapps.thebesacademy.Utils.ThebesInterface;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public abstract class RetrieveData<T> {
    Context context;
    List<T> listRetrieve;

    public RetrieveData(Context context) {
        this.context = context;
        listRetrieve = new ArrayList<>();
    }

    public void RetrieveList(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveList<T> callBackRetrieveList) {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (!hasKey(object, listRetrieve)) {
                        listRetrieve.add(0, object);
                        callBackRetrieveList.onDataList(listRetrieve);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (hasKey(object, listRetrieve)) {
                        int position = getPosition(object, listRetrieve);
                        listRetrieve.set(position, object);
                        callBackRetrieveList.onChangeList(listRetrieve, position);
                    } else {
                        onChildAdded(dataSnapshot, s);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    if (hasKey(object, listRetrieve)) {
                        int position = getPosition(object, listRetrieve);
                        listRetrieve.remove(position);
                        callBackRetrieveList.onRemoveFromList(position);
                    }
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void RetrieveSingleTimes(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    //error
                    callBackRetrieveTimes.onData(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void RetrieveEventTimes(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    T object = dataSnapshot.getValue(tClass);
                    callBackRetrieveTimes.onData(object);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void RetrieveSingleTimesRepeat(final Class<T> tClass, DatabaseReference reference, final CallBackRetrieveTimes<T> callBackRetrieveTimes) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        T object = data.getValue(tClass);
                        callBackRetrieveTimes.onData(object);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void hasUser(final Class<T> tClass, final DatabaseReference databaseReference, final ThebesInterface.CallBackFindUser<T> callBackFindUser) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        T object = data.getValue(tClass);
                        callBackFindUser.hasUser(object);
                    }
                } else {
                    callBackFindUser.hasUser(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public boolean hasKey(T object, List<T> listRetrieve) {
        return false;
    }

    public int getPosition(T object, List<T> listRetrieve) {
        return 0;
    }

    public interface CallBackRetrieveList<T> {
        void onDataList(List<T> object);

        void onChangeList(List<T> object, int position);

        void onRemoveFromList(int removePosition);
    }

    public interface CallBackRetrieveTimes<T> {
        void onData(T objects);
    }
}
