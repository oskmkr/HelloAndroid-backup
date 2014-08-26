package io.oskm.helloandroid.chat;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.oskm.helloandroid.R;

public class RoomActivity extends Activity {
    private Socket socket = null;
    List<String> msgList = null;
    ArrayAdapter<String> adapter = null;
    Handler handler = null;
    ListView msglistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        msglistView = (ListView) findViewById(R.id.msgListView);
        //SimpleAdapter adapter = new SimpleAdapter();
        msgList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgList);
        msglistView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //msgList.add();
                adapter.notifyDataSetChanged();
                //msglistView.scrollTo(0, msglistView.getHeight());
                msglistView.smoothScrollToPosition(msglistView.getHeight());
            }
        };


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    socket = new Socket("54.64.84.87", 8023);

                    Thread t = new Thread(new SocketListener(socket, msgList, handler));
                    t.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null, null);


        Button msgSendButton = (Button) findViewById(R.id.msgSendBtn);

        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText msgInput = (EditText) findViewById(R.id.msgInput);
                String msg = msgInput.getText().toString();

                msgInput.setText("");

                Log.d("chat", msg);

                Toast.makeText(RoomActivity.this, msg, Toast.LENGTH_LONG).show();

                (new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... msg) {

                        BufferedWriter outWriter = null;
                        try {

                            outWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                            outWriter.write(msg[0] + "\r\n");
                            outWriter.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).execute(msg);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
