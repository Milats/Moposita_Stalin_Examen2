package com.fisei.athanasiaapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.fisei.athanasiaapp.ui.ShopCartFragment;

public class CheckoutDialogFragment extends AppCompatDialogFragment {
    public Boolean button = false;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?")
                .setMessage("Order will be completed")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            ShopCartFragment quizFragment = (ShopCartFragment) getParentFragment();
                            try{
                                quizFragment.ExecuteSaleTask();
                            }catch (Exception e){
                                Log.e("s","Unable to call ExecuteSaleTask()", e);
                            }
                        }
                        catch (Exception e){
                            Log.e("s","Unable to get ShopCartFragment", e);
                        }
                    }
                })
        .setNegativeButton("Cancel", null);
        return builder.create();
    }
}