package net.sourceforge.simcpux.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.github.promeg.pinyinhelper.Pinyin;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.bean.ContactBean;
import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.view.AlphabetSlideBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AlphabetActivity extends BaseActivity {

    private static final String TAG = "AlphabetActivity";
    private ListView lv;
    private ArrayList<ContactBean> contactList = new ArrayList<>();
    private AlphabetSlideBar alphabetSlideBar;
    private ContactAdapter contactAdapter;
    private TextView tv_key;

    @Override
    protected int getResId() {
        return R.layout.activity_alphabet;
    }

    @NonNull
    @Override
    protected String[] requestPermissions() {
        return new String[]{Manifest.permission.READ_CONTACTS};
    }

    @Override
    protected void initData() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String pinyin = Pinyin.toPinyin(name, "");
                ContactBean contactBean = new ContactBean(name, pinyin);
                contactList.add(contactBean);
            }
        }

        contactAdapter = new ContactAdapter(contactList);
        lv.setAdapter(contactAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AlphabetActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initView() {
        lv = findViewById(R.id.lv);
        tv_key = findViewById(R.id.tv_key);
        alphabetSlideBar = findViewById(R.id.alphabet_slidebar);
        alphabetSlideBar.setOnSlideTouchLisenter(new AlphabetSlideBar.OnSlideTouchLisenter() {
            @Override
            public void onSlideTouchDown() {
                tv_key.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSlideTouchMove(int section) {
                Character key = contactAdapter.getSections()[section];
                tv_key.setText(String.valueOf(key));
                int position = contactAdapter.getPositionForSection(section);
                if (position != -1) {
                    lv.setSelection(position);
                }
            }

            @Override
            public void onSlideTouchUp() {
                tv_key.setVisibility(View.GONE);
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AlphabetActivity.class);
    }

    private class ContactAdapter extends BaseAdapter implements SectionIndexer {
        private ArrayList<ContactBean> contactList = new ArrayList<>();
        //字母集合
        private ArrayList<ContactBean> alphabetList = new ArrayList<>();
        //非字母集合
        private ArrayList<ContactBean> noAlphabetList = new ArrayList<>();
        private HashMap<Character, Integer> alphabetMap = new HashMap<>();
        private ViewHolder viewHolder;

        public ContactAdapter(ArrayList<ContactBean> list) {
            //1.区分字母/非字母
            for (ContactBean bean : list) {
                if (Character.isLetter(bean.getSectionKey())) {
                    alphabetList.add(bean);
                } else {
                    noAlphabetList.add(bean);
                }
            }

            //2.字母list按拼音排序
            Collections.sort(alphabetList, new Comparator<ContactBean>() {
                @Override
                public int compare(ContactBean o1, ContactBean o2) {
                    return o1.getPinyin().compareTo(o2.getPinyin());
                }
            });

            //3.整合字母list和非字母list
            contactList.addAll(alphabetList);
            contactList.addAll(noAlphabetList);

            //4.存储section-position
            for (int i = 0; i < contactList.size(); i++) {
                ContactBean contactBean = contactList.get(i);
                char ch = contactBean.getSectionKey();
                L.i(TAG, "ch: " + ch);
                if (!alphabetMap.containsKey(ch)) {
                    alphabetMap.put(ch, i);
                }
            }
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.item_contact, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_section = convertView.findViewById(R.id.tv_section);
                viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ContactBean contactBean = contactList.get(position);
            char key = contactList.get(position).getSectionKey();
            Integer section = alphabetMap.get(key);
            L.i(TAG, "section: " + section + "---name: " + contactBean.getName());
            if (section == position) {
                //section第一个item
                viewHolder.tv_section.setVisibility(View.VISIBLE);
                viewHolder.tv_section.setText(String.valueOf(key));
            } else {
                //section其他item
                viewHolder.tv_section.setVisibility(View.GONE);
            }
            viewHolder.tv_name.setText(contactBean.getName());
            return convertView;
        }

        class ViewHolder {
            TextView tv_section;
            TextView tv_name;
        }

        @Override
        public Character[] getSections() {
            return new Character[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                    'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            Integer postion = alphabetMap.get(getSections()[sectionIndex]);
            if (postion == null) {
                return -1;
            }
            return postion;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }
    }
}
