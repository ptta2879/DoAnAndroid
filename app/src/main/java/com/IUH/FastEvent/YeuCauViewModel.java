package com.IUH.FastEvent;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.IUH.FastEvent.Model.YeuCau;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YeuCauViewModel extends ViewModel {
    private MutableLiveData<ArrayList<YeuCau>> mutableLiveData;
    private FirebaseFirestore db;
    private static final String TAG ="fi";
    private ListenerRegistration listenerRegistration;

    public YeuCauViewModel() {
        db = FirebaseFirestore.getInstance();
        mutableLiveData = new MutableLiveData<>();
    }
    public void setMutableLiveData(ArrayList<YeuCau> yeuCaus){
        mutableLiveData.setValue(yeuCaus);
    }
    public MutableLiveData<ArrayList<YeuCau>>  getMutableLiveData(){
        return mutableLiveData;
    }
}
