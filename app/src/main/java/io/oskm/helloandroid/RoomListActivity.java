package io.oskm.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomListActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        Intent intent = getIntent();

        String nickName = intent.getStringExtra("nickName");

        List<String> listViewItemList = new ArrayList<String>();

        for (int i = 0; i < 20; i++) {
            listViewItemList.add("리스트 아이템" + i + 1);
        }

        ListView roomListView = (ListView) findViewById(R.id.listView);
        String[] listItems = new String[0];

        ListAdapter adapter = new ArrayAdapter<String>(RoomListActivity.this, android.R.layout.simple_list_item_1, listViewItemList.toArray(listItems));
        roomListView.setAdapter(adapter);


        List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> roomList = new ArrayList<List<Map<String, String>>>();

        Map<String, String> groupField = new HashMap<String, String>();

        groupField.put("groupName", "그룹명");

        groupList.add(groupField);
        groupList.add(groupField);
        groupList.add(groupField);
        groupList.add(groupField);

        List<Map<String, String>> roomFieldList = new ArrayList<Map<String, String>>();

        Map<String, String> roomField = new HashMap<String, String>();
        roomField.put("roomNameKor", nickName);
        roomField.put("roomNameEng", "roomName");

        roomFieldList.add(roomField);
        roomFieldList.add(roomField);

        roomList.add(roomFieldList);
        roomList.add(roomFieldList);
        roomList.add(roomFieldList);
        roomList.add(roomFieldList);

        ExpandableListAdapter expandableListAdapter = new SimpleExpandableListAdapter(
                this, groupList, android.R.layout.simple_expandable_list_item_1, new String[]{"groupName"},
                new int[]{android.R.id.text1}, roomList, android.R.layout.simple_expandable_list_item_2,
                new String[]{"roomNameKor", "roomNameEng"}, new int[]{android.R.id.text1, android.R.id.text2}
        );

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        expandableListView.setAdapter(expandableListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
