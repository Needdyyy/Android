package com.needyyy.app.Modules.Dating.suggestions.fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.needyyy.app.Base.BaseFragment;
import com.needyyy.app.R;
import com.needyyy.app.views.MyRangeSeekbar;
import androidx.annotation.Nullable;

public class FilterFragment extends BaseFragment {
    TextView from_km;
    TextView to_km;
    TextView from_year;
    TextView to_year;
    SeekBar seekBar;
    MyRangeSeekbar myRangeSeekbar;
    EditText active_listening_skill;
    RadioGroup radioGender;
    RadioButton male, female, both;
    int gender;
    Button apply;
    private int rangeMin = 0;
    private int rangeMax = 0;
    public int setDistance;
    public int setFromAge;
    public int setToAge;

    public FilterFragment() {
    }

    public static FilterFragment newInstance() {
        FilterFragment filterFragment = new FilterFragment();
        return filterFragment;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_filter);
    }

    protected void initView(View mView) {
        from_km = mView.findViewById(R.id.tv_km1);
        to_km = mView.findViewById(R.id.tv_km2);
        from_year = mView.findViewById(R.id.tv_year1);
        to_year = mView.findViewById(R.id.tv_year2);
        seekBar = mView.findViewById(R.id.tv_seekBar1);
        myRangeSeekbar = mView.findViewById(R.id.tv_seekBar2);
        male = mView.findViewById(R.id.radio_male);
        female = mView.findViewById(R.id.radio_female);
        both = mView.findViewById(R.id.radio_both);
        apply = mView.findViewById(R.id.button_apply);
        radioGender = mView.findViewById(R.id.radio_gender);
    }

    @Override
    protected void bindControls(Bundle savedInstanceState) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setDistance=i;
                to_km.setText(i+" "+"km");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        myRangeSeekbar.setMinValue(18);
        myRangeSeekbar.setMaxValue(60);
        myRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                rangeMin = minValue.intValue();
                rangeMax = maxValue.intValue();

                setFromAge=rangeMin;
                setToAge=rangeMax;

                from_year.setText(String.valueOf(rangeMin));
                to_year.setText(String.valueOf(rangeMax));

                Log.e("rangeListener", "" + rangeMin);
                Log.e("rangeListener", "" + rangeMax);
            }
        });

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radio_both) {
                    gender=0 ;
                } else if(checkedId == R.id.radio_male) {
                    gender=1 ;
                } else if(checkedId == R.id.radio_female){
                    gender=2 ;
                }
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("Distance",Integer.toString(setDistance));
                bundle.putString("From_age",Integer.toString(setFromAge));
                bundle.putString("To_age",Integer.toString(setToAge));
                bundle.putString("Gender", Integer.toString(gender));

                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                FriendSuggestion friendSuggestion=new FriendSuggestion();
                friendSuggestion.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_main,friendSuggestion);
                fragmentTransaction.commit();

             //   ((HomeActivity)getActivity()).replaceFragment(FriendSuggestion.newInstance());
            }
        });
    }
}

























//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_filter, container, false);
//        return v;
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View mView, @Nullable Bundle savedInstanceState) {
//        from_km = mView.findViewById(R.id.tv_km1);
//        to_km = mView.findViewById(R.id.tv_km2);
//        from_year = mView.findViewById(R.id.tv_year1);
//        to_year = mView.findViewById(R.id.tv_year2);
//        seekBar = mView.findViewById(R.id.tv_seekBar1);
//        myRangeSeekbar = mView.findViewById(R.id.tv_seekBar2);
//        active_listening_skill = mView.findViewById(R.id.tv_listening_skill);
//        male = mView.findViewById(R.id.radio_male);
//        female = mView.findViewById(R.id.radio_female);
//        both = mView.findViewById(R.id.radio_both);
//        apply = mView.findViewById(R.id.button_apply);
//        radioGender = mView.findViewById(R.id.radio_gender);
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                setDistance=i;
//                to_km.setText(i+" "+"km");
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//
//        myRangeSeekbar.setMinValue(18);
//        myRangeSeekbar.setMaxValue(60);
//        myRangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue, Number maxValue) {
//                rangeMin = minValue.intValue();
//                rangeMax = maxValue.intValue();
//
//                setFromAge=rangeMin;
//                setToAge=rangeMax;
//
//                from_year.setText(String.valueOf(rangeMin));
//                to_year.setText(String.valueOf(rangeMax));
//
//                Log.e("rangeListener", "" + rangeMin);
//                Log.e("rangeListener", "" + rangeMax);
//            }
//        });
//
//        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if(checkedId == R.id.radio_both) {
//                    gender=0 ;
//                } else if(checkedId == R.id.radio_male) {
//                    gender=1 ;
//                } else if(checkedId == R.id.radio_female){
//                    gender=2 ;
//                }
//            }
//        });
//
//        apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ((HomeActivity)getActivity()).replaceFragment(FriendSuggestion.newInstance());
////                String s1=from_km.getText().toString();
////                String s2=to_km.getText().toString();
////                String s3=from_year.getText().toString();
////                String s4=to_year.getText().toString();
////                String s5=male.getText().toString();
////                String s6=female.getText().toString();
////                String s7=both.getText().toString();
////
////                Intent i = new Intent();
////                i.putExtra("from_km",s1);
////                i.putExtra("to_km",s2);
////                i.putExtra("from_year",s3);
////                i.putExtra("to_year",s4);
////                i.putExtra("male",s5);
////                i.putExtra("female",s6);
////                i.putExtra("both",s7);
////                if (getTargetFragment() != null) {
////                    Fragment fragment=((HomeActivity)getActivity()).getSupportFragmentManager().findFragmentById(R.id.frame_main);
////                    fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
////                }
//
//            }
//        });
//      }





