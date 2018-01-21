package jp.techacademy.hongou.yuka.taskapp;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;

        import io.realm.Realm;
        import io.realm.RealmChangeListener;
        import io.realm.RealmResults;
        import io.realm.Sort;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_TASK = "jp.techacademy.hongou.yuka.taskapp.TASK";

    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
            Log.d("Task", "onChange");
        }
    };
    private ListView mListView;
    private TaskAdapter mTaskAdapter;
    private Button mSortButton;
    private Button mAllDispButton;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSortButton = (Button) findViewById(R.id.sort_button);
        mSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditTextの入力値を取得
                mEditText = (EditText)findViewById(R.id.sort_edit_text);
                String category = mEditText.getText().toString();

                //Realmで検索
                final Task task = new Task();
                RealmResults<Task> results = mRealm.where(Task.class).equalTo("category", category).findAll();

                mTaskAdapter.setTaskList(mRealm.copyFromRealm(results));
                // TaskのListView用のアダプタに渡す
                mListView.setAdapter(mTaskAdapter);
                // 表示を更新するために、アダプターにデータが変更されたことを知らせる
                mTaskAdapter.notifyDataSetChanged();
                }
        });

        mAllDispButton = (Button) findViewById(R.id.all_button);
        mAllDispButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadListView();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });

            // Realmの設定
            mRealm=Realm.getDefaultInstance();
            mRealm.addChangeListener(mRealmListener);


            // ListViewの設定
            mTaskAdapter=new

            TaskAdapter(MainActivity.this);

            mListView=(ListView)

            findViewById(R.id.listView1);


            // ListViewをタップしたときの処理
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

            {
                @Override
                public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                // 入力・編集する画面に遷移させる
                Task task = (Task) parent.getAdapter().getItem(position);

                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                intent.putExtra(EXTRA_TASK, task.getId());

                startActivity(intent);
            }
            }

            );

            //ListViewを長押ししたときの処理
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

            {
                @Override
                public boolean onItemLongClick (AdapterView < ? > parent, View view,int position,
                long id){
                // タスクを削除する

                final Task task = (Task) parent.getAdapter().getItem(position);

                //ダイアログを表示する
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("削除");
                builder.setMessage(task.getTitle() + "を削除しますか");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RealmResults<Task> results = mRealm.where(Task.class).equalTo("id", task.getId()).findAll();

                        mRealm.beginTransaction();
                        results.deleteAllFromRealm();
                        mRealm.commitTransaction();

                        reloadListView();

                    }
                });
                builder.setNegativeButton("CANCEL", null);

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
            }

            );

            reloadListView();
        }

        //検索窓でカテゴリーを検索した時の処理


    private void reloadListView() {
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        RealmResults<Task> taskRealmResults = mRealm.where(Task.class).findAllSorted("date", Sort.DESCENDING);
        // 上記の結果を、TaskList としてセットする
        mTaskAdapter.setTaskList(mRealm.copyFromRealm(taskRealmResults));
        // TaskのListView用のアダプタに渡す
        mListView.setAdapter(mTaskAdapter);
        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}