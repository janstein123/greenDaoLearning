package com.gentle.greendaolearning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentle.greendaolearning.model.DaoSession;
import com.gentle.greendaolearning.model.User;
import com.gentle.greendaolearning.model.UserDao;

import java.util.List;
import java.util.Random;

public class UsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private static final String TAG = "UsersActivity";
    private ListView mUserListView;
    private List<User> mUsers;
    private UserAdapter mUserAdapter;

    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaoSession session = ((MyApplication)getApplication()).getDaoSession();
        mUserDao = session.getUserDao();
        mUsers = mUserDao.loadAll();

        mUserListView = (ListView) findViewById(R.id.user_list);
        mUserAdapter = new UserAdapter();
        mUserListView.setAdapter(mUserAdapter);
        mUserListView.setOnItemClickListener(this);
        mUserListView.setOnItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_user_operations, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    Random random = new Random(100);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Log.d(TAG, "add user");
            int i = Math.abs(random.nextInt());
            String name = "user"+i;
            String email = name+"@tcl.com";
            boolean isOnline = random.nextBoolean();
            mUserDao.insert(new User(i, name, email, isOnline));
            Toast.makeText(UsersActivity.this, "Add successfully.", Toast.LENGTH_SHORT).show();
            mUsers = mUserDao.loadAll();
            mUserAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User)mUserAdapter.getItem(position);
        mUserDao.delete(user);
        Toast.makeText(UsersActivity.this, "Delete successfully.", Toast.LENGTH_SHORT).show();
        mUsers = mUserDao.loadAll();
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final User user = (User) mUserAdapter.getItem(position);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_rename_user, null);
        final EditText nameEdit = (EditText) v.findViewById(R.id.name_edit);
        nameEdit.setText(user.getName());
        nameEdit.setSelection(user.getName().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.setTitle("Rename User");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEdit.getText().toString().trim();
                if(name != null && !name.isEmpty() && !name.equals(user.getName())){
                    user.setName(name);
                    mUserDao.update(user);
                    Toast.makeText(UsersActivity.this, "Rename successfully.", Toast.LENGTH_SHORT).show();
                    mUsers = mUserDao.loadAll();
                    mUserAdapter.notifyDataSetChanged();
                }else if(name.equals(user.getName())){
                    Toast.makeText(UsersActivity.this, "Name is not changed.", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty()){
                    Toast.makeText(UsersActivity.this, "New name is missing.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        return true;
    }

    private class UserAdapter extends BaseAdapter{
        private LayoutInflater mmInflater;
        public UserAdapter() {
            mmInflater = LayoutInflater.from(UsersActivity.this);
        }

        @Override
        public int getCount() {
            return mUsers.size();
        }

        @Override
        public Object getItem(int position) {
            return mUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView position = "+position+", convertView = "+convertView);
            if(convertView == null){
                convertView = mmInflater.inflate(R.layout.user_list_item, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.tv_personal_name);
                holder.email = (TextView) convertView.findViewById(R.id.tv_email);
                holder.isOnline = (TextView) convertView.findViewById(R.id.tv_is_online);
                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.name.setText(mUsers.get(position).getName());
            holder.email.setText(mUsers.get(position).getEmail());
            holder.isOnline.setText(mUsers.get(position).isOnline() ? "online" : "offline");

            return convertView;
        }

        class ViewHolder{
            TextView name;
            TextView email;
            TextView isOnline;
        }
    }
}
