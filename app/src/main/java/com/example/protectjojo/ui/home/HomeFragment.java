package com.example.protectjojo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.protectjojo.R;
import com.example.protectjojo.databinding.FragmentHomeBinding;
import com.example.protectjojo.ruleview.RulerHelper;
import com.example.protectjojo.ruleview.ScrollRulerLayout;
import com.example.protectjojo.ruleview.ScrollSelected;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private String currentSelectd = "37.0";

    private TextView mTempTextView ;
    private LinearLayout mlayout ;
    private ScrollRulerLayout rulerView ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mTempTextView = root.findViewById(R.id.textView);
        mlayout = root.findViewById(R.id.ruler_layout);
        rulerView =  root.findViewById(R.id.ruler_view);

        rulerView.setScope(34,45, 1F);
        rulerView.setCurrentItem("37.0");



        rulerView.setSroollSelectedListener(new ScrollSelected() {
            @Override
            public void selected(String selected) {
                //Toast.makeText(getContext(), selected, Toast.LENGTH_SHORT).show();
                currentSelectd = selected;
                String selectedText = selected +"°C";
                mTempTextView.setText(selectedText);

                float floatValue = Float.parseFloat(currentSelectd);
                int currentColor = RulerHelper.getBodyTempColor(floatValue);
                //mlayout.setBackgroundColor(currentColor);

                mTempTextView.setTextColor(currentColor);

            }
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentSelectd = rulerView.getCurrentText();
        rulerView.setCurrentItem(currentSelectd);
        int currentColor = RulerHelper.getBodyTempColor(Float.parseFloat(currentSelectd));
        mTempTextView.setTextColor(currentColor);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}