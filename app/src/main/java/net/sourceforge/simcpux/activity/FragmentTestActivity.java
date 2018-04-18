package net.sourceforge.simcpux.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.fragment.AnotherFragment;
import net.sourceforge.simcpux.fragment.LeftFragment;
import net.sourceforge.simcpux.fragment.RightFragment;

public class FragmentTestActivity extends AppCompatActivity implements LeftFragment.OnClickLisenter {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        replaceFragment(new RightFragment());
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FragmentTestActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onClick() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag_content);
        if(fragment != null && !(fragment instanceof AnotherFragment)){
            replaceFragmentBack(new AnotherFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_content, fragment).commit();
    }

    private void replaceFragmentBack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_content, fragment).addToBackStack(null).commit();
    }
}
