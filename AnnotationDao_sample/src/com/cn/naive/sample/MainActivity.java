package com.cn.naive.sample;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cn.naive.library.dao.AnnotationDao;
import com.cn.naive.sample.model.Dog;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final AnnotationDao<Dog> dao = new AnnotationDao<Dog>(this, Dog.class);
		List<Dog> list = dao.list();

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}

		ListView listView = (ListView) findViewById(R.id.listview);
		final ArrayAdapter<Dog> adapter = new ArrayAdapter<Dog>(this, R.layout.list_item, list);
		listView.setAdapter(adapter);

		findViewById(R.id.button_insert).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < 10; i++) {
					String name = "name" + i;
					boolean alive = Math.random() * 10 % 3 == 0 ? true : false;

					Dog dog = new Dog(alive, name);
					dao.insert(dog);
					adapter.add(dog);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
}
