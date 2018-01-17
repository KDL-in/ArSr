package com.arsr.arsr.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.arsr.arsr.R;
import com.arsr.arsr.activity.ActivityCollector;
import com.arsr.arsr.activity.SettingActivity;
import com.arsr.arsr.util.DrawableUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private AppCompatActivity context;
    private CircleImageView blueBtn;
    private CircleImageView redBtn;
    private TextView debugBtn,aboutBtn;
    public SettingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SettingFragment(AppCompatActivity context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        blueBtn.setOnClickListener(this);
        redBtn.setOnClickListener(this);
        debugBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
        return view;
    }

    private void init(View view) {
        blueBtn = view.findViewById(R.id.imgBtn_blue);
        redBtn = view.findViewById(R.id.imgBtn_red);
        debugBtn = view.findViewById(R.id.btn_setting_debug);
        aboutBtn = view.findViewById(R.id.btn_setting_about);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_blue:
                DrawableUtil.setTheme(R.style.AppTheme);
                ActivityCollector.finishAll();
                break;
            case R.id.imgBtn_red:
                DrawableUtil.setTheme(R.style.RedTheme);
                ActivityCollector.finishAll();
                break;
            case R.id.btn_setting_debug:
                ((SettingActivity) context).showLog();
                break;
            case R.id.btn_setting_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("设计理念");
                builder.setMessage("https://github.com/KDL-in/ArSr\n"+"    不要害怕任务太多很快忘记，记忆的核心在于重复。"+"\n    艾宾浩斯在一个多世纪前只提出了遗忘曲线（Forgetting curve），所以艾宾浩斯曲线记忆，或者艾宾浩斯曲线记忆周期应该是一个伪概念。简单说艾宾浩斯遗忘曲线规律就是：记忆随着时间的流逝，时间越长，遗忘的比例就越高，也就是说短期记忆能够记住的信息的比例很高，而时间越长，遗忘的就越多。因此，记忆规律并不是不变的，随着记忆对象以及记忆者的不同而不同。\n" +
                        "后人在遗忘曲线理论的基础上，探索出了一些实用的记忆方法，其中已经被学界公认的并且应用到实践中的有两个，一个叫做主动回忆（active recall ），主动回忆是指，每次在复习的时候，不是重新阅读一遍全部的问题和与之对应的答案（或者是全部的信息），而是只看问题，然后向自己的大脑询问答案（或者是需要记忆的那部分缺失的信息）。另一个是 Spaced repetition，这个词在中文中并没有对应的的概念，它的意思就是，在我们主动复习以前需要记忆过的内容的时候，复习的频率没必要永远保持一致，而是恰恰相反，如果能够每次检查复习的时候，如果确认自己已经记住了，则下一次复习时间可以发生在更加远一些的将来，如果确认自己没能成功记住，则下一次的复习时间可以定在比较近的将来。很多人或者组织都宣称了有效的这种周期的定义方法，但是，并没有一个公认普适有效的Spaced repetition周期。\n");
                builder.show();
                break;
        }
    }
}
