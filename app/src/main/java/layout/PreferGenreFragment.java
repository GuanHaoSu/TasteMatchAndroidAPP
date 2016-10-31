package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kwan.musicgraph.HttpRequest;
import com.example.kwan.musicgraph.JsonParser;
import com.example.kwan.musicgraph.GenreList;
import com.example.kwan.musicgraph.PreferMusicGenreAdapter;
import com.example.kwan.musicgraph.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.util.ArrayList;
/**
 * Created by Kwan on 2016/7/5.
 */
public class PreferGenreFragment extends Fragment {
    static ArrayList<GenreList> list_array = new ArrayList<>();
    static ArrayList<Integer> Scorelist_array = new ArrayList<>();
    static JsonParser jsonParser = new JsonParser();

    static ListView list;
    String ServerIP;
    String ID;
    public PreferGenreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServerIP=getResources().getString(R.string.ServerIP);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext(),new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {

                if(AccessToken.getCurrentAccessToken() == null){

                } else {
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prefer_genre, container,false);
        list = (ListView) v.findViewById(R.id.listView);
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

                    list_array=  jsonParser.GenreList(output);
                    PreferMusicGenreAdapter adapter = new PreferMusicGenreAdapter(list_array, getActivity().getBaseContext());
                    list.setAdapter(adapter);
                }
            }).execute("http://"+ServerIP+"/PreferMusicGenre/"+ID);
        }

    }
}