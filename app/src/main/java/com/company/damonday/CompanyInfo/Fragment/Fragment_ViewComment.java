package com.company.damonday.CompanyInfo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.company.damonday.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LAM on 20/4/2015.
 */
public class Fragment_ViewComment extends Fragment {

    private View v;
    private ListView commentListView;
    private Fragment_ViewComment_CustomListAdapter customListAdapter;
    private List<Fragment_ViewComment_Comment> commentList = new ArrayList<Fragment_ViewComment_Comment>();
    private Fragment_ViewComment_Comment comment1, comment2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        comment1 = new Fragment_ViewComment_Comment("Basketball", "http://androidbonuscasino.com/images/Android-logo2.png",
                "2015-07-25", "I am a hero. You are a dog", "Alan Lam");
        comment2 = new Fragment_ViewComment_Comment("Football", "http://androidbonuscasino.com/images/Android-logo2.png",
                "2015-07-25", "I am a hero. You are a dog", "Alan Lam");
        commentList.add(comment1);
        commentList.add(comment2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        v = inflater.inflate(R.layout.view_companycomment, container, false);

        commentListView = (ListView) v.findViewById(R.id.list);
        customListAdapter = new Fragment_ViewComment_CustomListAdapter(getActivity(), commentList);
        commentListView.setAdapter(customListAdapter);


        return v;
    }
}

