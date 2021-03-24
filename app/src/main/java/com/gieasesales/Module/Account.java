package com.gieasesales.Module;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.gieasesales.R;
import com.gieasesales.activity.HistoryActivity;
import com.gieasesales.activity.QuoteActivity;
import com.gieasesales.utils.CommonAlertDialog;
import com.gieasesales.utils.Util;
import com.gieasesales.activity.ChangePassword;
import com.gieasesales.activity.Login;
import com.gieasesales.activity.ManageAddress;
import com.gieasesales.activity.PaymentDueActivity;
import com.gieasesales.activity.Profile;
import com.gieasesales.activity.ScanQR;
import com.gieasesales.activity.Wishlist;


public class Account extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;

    LinearLayout LyProfile, LyChangePass, LyOffers, LyLogout, LyAddress, ly_wishlist, ly_qrscan, ly_quote, ly_history;
    ImageView back_arrow;

    CommonAlertDialog alert;

    public Account() {
        // Required empty public constructor
    }

    //register_now
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.account, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });

        // Programmatically initialize the scanner view

        // Inflate the layout for this fragment
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        alert = new CommonAlertDialog(getActivity());
        ly_qrscan = rootView.findViewById(R.id.ly_qrscan);
        LyProfile = rootView.findViewById(R.id.ly_profile);
        LyChangePass = rootView.findViewById(R.id.ly_change_pass);
        ly_wishlist = rootView.findViewById(R.id.ly_wishlist);
        LyOffers = rootView.findViewById(R.id.ly_offers);
        LyAddress = rootView.findViewById(R.id.ly_address);
        LyLogout = rootView.findViewById(R.id.ly_logout);
        ly_quote = rootView.findViewById(R.id.ly_quote);
        ly_history = rootView.findViewById(R.id.ly_history);
        back_arrow = rootView.findViewById(R.id.back_arrow);
        back_arrow.setVisibility(View.INVISIBLE);

        LyProfile.setOnClickListener(this);
        LyChangePass.setOnClickListener(this);
        LyOffers.setOnClickListener(this);
        LyLogout.setOnClickListener(this);
        LyAddress.setOnClickListener(this);
        ly_wishlist.setOnClickListener(this);
        ly_qrscan.setOnClickListener(this);
        ly_quote.setOnClickListener(this);
        ly_history.setOnClickListener(this);

        return rootView;

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mScannerView.stopCamera();
        if (!Util.ShowQRDATA.isEmpty()) {
            alert.build(Util.ShowQRDATA, true);
            Util.ShowQRDATA = "";
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_history:

                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent history = new Intent(getActivity(), HistoryActivity.class);
                    startActivity(history);
                }

                break;
            case R.id.ly_quote:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent quote = new Intent(getActivity(), QuoteActivity.class);
                    startActivity(quote);
                }
                break;
            case R.id.ly_qrscan:
                if (getActivity().getPackageName().equals("com.gieasesales")) {

                    Intent scanqr = new Intent(getActivity(), ScanQR.class);
                    startActivity(scanqr);
                }
                break;
            case R.id.ly_address:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent manageaddress = new Intent(getActivity(), ManageAddress.class);
                    startActivity(manageaddress);
                }
                break;
            case R.id.ly_profile:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent profile = new Intent(getActivity(), Profile.class);
                    profile.putExtra("finish", "false");
                    startActivity(profile);
                }
                break;
            case R.id.ly_change_pass:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent changepassword = new Intent(getActivity(), ChangePassword.class);
                    startActivity(changepassword);
                }

                break;
            case R.id.ly_wishlist:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent wishlist = new Intent(getActivity(), Wishlist.class);
                    startActivity(wishlist);
                }

                break;
            case R.id.ly_offers:
                if (getActivity().getPackageName().equals("com.gieasesales")) {
                    Intent paymentdue = new Intent(getActivity(), PaymentDueActivity.class);
                    startActivity(paymentdue);
                }

                break;
            case R.id.ly_logout:
                AlertDialog.Builder alertDialogBuilder;
                alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.alertDialog);
                alertDialogBuilder.setMessage(R.string.want_to_logout);
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (getActivity().getPackageName().equals("com.gieasesales")) {
                                    Intent logout = new Intent(getActivity(), Login.class);
                                    startActivity(logout);
                                    getActivity().finish();
                                }

                            }
                        });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                break;
            default:
                break;
        }

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }


}