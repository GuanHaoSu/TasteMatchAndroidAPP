package layout;

/**
 * Created by Kwan on 2016/7/5.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kwan.musicgraph.FriendList;
import com.example.kwan.musicgraph.HttpRequest;
import com.example.kwan.musicgraph.JsonParser;
import com.example.kwan.musicgraph.R;
import com.example.kwan.musicgraph.RecommendFriendsAdapter;
import com.example.kwan.musicgraph.RecommendFriendsProfileActivity;
import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.ArrayList;

/**
 * Created by Kwan on 2016/7/5.
 */
public class RecommendFriendsFragment extends Fragment implements AdapterView.OnItemClickListener {
    static ListView RFlist;
    static JsonParser jsonParser = new JsonParser();
    String ServerIP ;
    ArrayList<FriendList> list_array= new ArrayList<FriendList>();
    ArrayList<String> Friends_id_list= new ArrayList<>();
    String ID;
    public RecommendFriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServerIP=getResources().getString(R.string.ServerIP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendfriends, container,false);
        RFlist = (ListView) v.findViewById(R.id.FR_ListView);
        RFlist.setOnItemClickListener(RecommendFriendsFragment.this);
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(AccessToken.getCurrentAccessToken()!=null){
            String token = AccessToken.getCurrentAccessToken().getToken();
            Profile profile = Profile.getCurrentProfile();
            ID = profile.getId();
            new HttpRequest.HttpAsyncTask(null, new HttpRequest.HttpAsyncTask.AsyncResponse() {
                @Override
                public void processFinish(String output, View v,long elapsedTime) {

                    list_array=  jsonParser.FriendsRecommendNameList(output);
                    Friends_id_list = jsonParser.FriendsRecommendIDList(output);
                    RecommendFriendsAdapter adapter = new RecommendFriendsAdapter(list_array, getActivity().getBaseContext());
                    RFlist.setAdapter(adapter);
                }
            }).execute("http://"+ServerIP+"/RecommendFriends/"+ID);
        }

    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), RecommendFriendsProfileActivity.class);
        Bundle bundle = new Bundle();
        String RfirendID =Friends_id_list.get(position);
        String RfirendName =list_array.get(position).getGenreName();
        bundle.putString("userID", ID);
        bundle.putString("RfirendID", RfirendID);
        bundle.putString("RfirendName", RfirendName);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
