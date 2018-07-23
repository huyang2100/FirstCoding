package net.sourceforge.simcpux.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.sourceforge.simcpux.R;

public class SeriAnimActivity extends AppCompatActivity {

    private View first;
    private View second;
    private View third;
    private boolean isCancel;
    private ObjectAnimator secondHide;
    private ObjectAnimator thirdHide;
    private ObjectAnimator secondVisible;
    private ObjectAnimator thirdVisible;
    private AnimatorSet bothHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seri_anim);

        initView();
    }

    private void initView() {
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);

        secondHide = ObjectAnimator.ofFloat(second, "alpha", 1, 0);
        secondVisible = ObjectAnimator.ofFloat(second, "alpha", 0, 1);

        thirdHide = ObjectAnimator.ofFloat(third, "alpha", 1, 0);
        thirdVisible = ObjectAnimator.ofFloat(third, "alpha", 0, 1);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                isCancel = false;

                //2 3 渐变消失
                //2 渐变显示
                //3 渐变显示
                //2 3 渐变消失


                AnimatorSet set = new AnimatorSet();
                set.play(secondHide).with(thirdHide).before(secondVisible);

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(set).before(thirdVisible);
                animatorSet.start();

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        if (!isCancel) {
                            animatorSet.start();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);

                    }
                });
            }
        });

        findViewById(R.id.btn_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancel = true;
            }
        });

        bothHide = new AnimatorSet();
        bothHide.play(secondHide).with(thirdHide);

        findViewById(R.id.btn_start2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancel = false;
                bothHide.start();
            }
        });

        //2 and 3 hide
        //2 show
        //3 show
        //2 and 3 hide

        bothHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                secondVisible.start();
            }
        });

        secondVisible.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                thirdVisible.start();
            }
        });

        thirdVisible.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isCancel) {
                    bothHide.start();
                }
            }
        });


    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SeriAnimActivity.class);
    }
}
