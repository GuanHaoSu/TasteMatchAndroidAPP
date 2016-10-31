package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kwan.musicgraph.HttpRequest;
import com.example.kwan.musicgraph.JsonParser;
import com.example.kwan.musicgraph.R;
import com.example.kwan.musicgraph.RecommendArtistAdapter;
import com.example.kwan.musicgraph.RecommendArtistsProfileActivity;
import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendArtistFragment extends Fragment  {
    static ListView list1;
    static JsonParser jsonParser = new JsonParser();
    String ServerIP ;
    ArrayList<String> list_array= new ArrayList<>();
    ArrayList<String> Friends_id_list= new ArrayList<>();
    String ID;
    static int[] btnState;
    public RecommendArtistFragment() {
    }
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), RecommendArtistsProfileActivity.class);
            Bundle bundle = new Bundle();
            String ArtistName =list_array.get(position);
            bundle.putString("userID", ID);
            bundle.putString("ArtistName", ArtistName);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServerIP=getResources().getString(R.string.ServerIP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_artist, container,false);
        list1 = (ListView) v.findViewById(R.id.FA_ListView);
        list1.setOnItemClickListener(onItemClickListener);
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

                    list_array=  jsonParser.RecommendArtistList(output);
                    int size=list_array.size();
                    btnState = new int[size];
                    for(int i=0;i<list_array.size();i++){
                        btnState[i]=0;
                    }
                    RecommendArtistAdapter adapter = new RecommendArtistAdapter(list_array,btnState, getActivity().getBaseContext(),ServerIP,ID);
                    list1.setAdapter(adapter);
                   // Toast.makeText(this, elapsedTime, Toast.LENGTH_LONG).show();
                }
            }).execute("http://"+ServerIP+"/RecommendArtists/"+ID);
        }

    }

}
