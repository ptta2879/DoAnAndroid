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
        listenYeuCau();
    }
    public MutableLiveData<ArrayList<YeuCau>>  getMutableLiveData(){
        return mutableLiveData;
    }
    public void stopListenYeuCau(){
        listenerRegistration.remove();
    }
    private void listenYeuCau(){
        listenerRegistration = db.collection("yeucau").whereEqualTo("trangthai",0)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e(TAG,error.toString());
                        }
                        ArrayList<YeuCau> yeuCaus = new ArrayList<>();
                        for (DocumentSnapshot doc : value){
                            YeuCau yeuCau = doc.toObject(YeuCau.class);
                            if (yeuCau != null){
                                yeuCaus.add(yeuCau);
                            }
                        }
                        mutableLiveData.setValue(yeuCaus);
                    }
                });
    }
}
