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
import com.gentle.greendaolearning.model.Home;
import com.gentle.greendaolearning.model.HomeDao;
import com.gentle.greendaolearning.model.User;
import com.gentle.greendaolearning.model.UserDao;

import java.util.List;
import java.util.Random;

public class UsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private static final String TAG = "UsersActivity";
    private ListView mUserListView;
    private ListView mHomeListView;
    private List<User> mUsers;
    private List<Home> mHomes;
    private UserAdapter mUserAdapter;
    private HomeAdapter mHomeAdapter;

    private UserDao mUserDao;
    private HomeDao mHomeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaoSession session = ((MyApplication)getApplication()).getDaoSession();
        mUserDao = session.getUserDao();
        mHomeDao = session.getHomeDao();
        mUsers = mUserDao.loadAll();
        mHomes = mHomeDao.loadAll();

        mUserListView = (ListView) findViewById(R.id.user_list);
        mHomeListView = (ListView) findViewById(R.id.home_list);
        mUserAdapter = new UserAdapter();
        mHomeAdapter = new HomeAdapter();
        mUserListView.setAdapter(mUserAdapter);
        mHomeListView.setAdapter(mHomeAdapter);
        mUserListView.setOnItemClickListener(this);
        mHomeListView.setOnItemClickListener(this);
        mUserListView.setOnItemLongClickListener(this);
        mHomeListView.setOnItemLongClickListener(this);
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

    int homeId = 1000;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_user) {
            Log.d(TAG, "add home");
            long i = System.currentTimeMillis();
            String name = "user"+i;
            String email = name+"@tcl.com";

            Random random = new Random(100);
            boolean isOnline = random.nextBoolean();

            User user = new User(i, name, email, isOnline, homeId);
            mUserDao.insert(user);

            for (Home home: mHomes){
                home.resetUsers();
            }
            
            mUsers = mUserDao.loadAll();

            mHomes = mHomeDao.loadAll();
            mUserAdapter.notifyDataSetChanged();
            mHomeAdapter.notifyDataSetChanged();
            return true;
        }else if(id == R.id.action_add_home){
            Log.d(TAG, "add home");
            long i = System.currentTimeMillis();
            String name = "user"+i;
            String email = name+"@tcl.com";

            Random random = new Random(100);
            boolean isOnline = random.nextBoolean();

            User user = new User(i, name, email, isOnline, homeId);
            mUserDao.insert(user);

            String homeName = "home"+homeId;
            Home home = new Home(homeId, homeName, user.getUid());
            mHomeDao.insert(home);
            Toast.makeText(UsersActivity.this, "Add successfully.", Toast.LENGTH_SHORT).show();
            mUsers = mUserDao.loadAll();
            mHomes = mHomeDao.loadAll();
            mUserAdapter.notifyDataSetChanged();
            mHomeAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.user_list) {
            User user = (User) mUserAdapter.getItem(position);
            mUserDao.delete(user);
            Toast.makeText(UsersActivity.this, "Delete successfully.", Toast.LENGTH_SHORT).show();
            mUsers = mUserDao.loadAll();
            mUserAdapter.notifyDataSetChanged();
        }else if(parent.getId() == R.id.home_list){
            Home home = (Home) mHomeAdapter.getItem(position);
            mHomeDao.delete(home);
            Toast.makeText(UsersActivity.this, "Delete successfully.", Toast.LENGTH_SHORT).show();
            mHomes = mHomeDao.loadAll();
            mHomeAdapter.notifyDataSetChanged();
        }
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

    private class HomeAdapter extends BaseAdapter{
        private LayoutInflater mmInflater;
        public HomeAdapter() {
            mmInflater = LayoutInflater.from(UsersActivity.this);
        }

        @Override
        public int getCount() {
            return mHomes.size();
        }

        @Override
        public Object getItem(int position) {
            return mHomes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView position = "+position+", convertView = "+convertView);
            if(convertView == null){
                convertView = mmInflater.inflate(R.layout.home_list_item, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.tv_home_name);
                holder.ownerName = (TextView) convertView.findViewById(R.id.tv_owner_name);
                holder.userSize = (TextView) convertView.findViewById(R.id.tv_users_size);
                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.name.setText(mHomes.get(position).getHomeName());
            holder.ownerName.setText(mHomes.get(position).getOwner().getName());
            List<User> users = mHomes.get(position).getUsers();
            holder.userSize.setText(users.size()+"");

            return convertView;
        }

        class ViewHolder{
            TextView name;
            TextView ownerName;
            TextView userSize;
        }
    }
}
