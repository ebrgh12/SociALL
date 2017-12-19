package com.gire.socialapplication.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gire.socialapplication.R;
import com.gire.socialapplication.adapter.ChatAdpater;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.model.ChatMessageModel;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.gire.socialapplication.utils.ProgressIndicator;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.gire.socialapplication.constants.APIConstants.GET_USER_MESSAGE;
import static com.gire.socialapplication.constants.APIConstants.MESSAGE_FRIEND;

/**
 * Created by girish on 7/22/2017.
 */

public class ChatFragment extends Fragment implements WebInterface{

    View view;
    RecyclerView userChat;
    EditText messageContent;
    TextView sendMessage;

    Handler userCharGetHandler;
    ProgressIndicator progressIndicator;
    WebServiceController webServiceController;

    String friendId;
    List<ChatMessageModel> chatMessageModels = new ArrayList<ChatMessageModel>();

    ChatAdpater chatAdpater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getArguments();
        if(extras != null) {
            friendId = extras.getString("friend_id");
        }

        webServiceController = new WebServiceController(getActivity(),ChatFragment.this);
        progressIndicator = new ProgressIndicator();
        progressIndicator.showProgress(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_chat_view,container,false);
        userChat = (RecyclerView) view.findViewById(R.id.user_chat);
        userChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        messageContent = (EditText) view.findViewById(R.id.message_content);
        sendMessage = (TextView) view.findViewById(R.id.send_button);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageContent.getText().toString() != null &&
                        !messageContent.getText().toString().isEmpty()){

                    chatMessageModels.add(new ChatMessageModel(2,messageContent.getText().toString()));
                    chatAdpater.notifyDataSetChanged();

                    progressIndicator.showProgress(getActivity());
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("sender_id",SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
                    requestParams.put("reciever_id",friendId);
                    requestParams.put("message",messageContent.getText().toString());
                    webServiceController.postRequest(MESSAGE_FRIEND,requestParams,2);

                    messageContent.setText("");
                }else {
                    Toast.makeText(getActivity(), "Please enter message to send", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createHandler();
        return view;
    }

    private void createHandler() {
        userCharGetHandler = new Handler();
        userCharGetHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestParams requestParams = new RequestParams();
                requestParams.put("reciever_id", SharedPreferencesUtil.getInstance(getActivity()).getCustomerID());
                requestParams.put("sender_id",friendId);
                webServiceController.postRequest(GET_USER_MESSAGE,requestParams,1);
                userCharGetHandler.postDelayed(this,20000);
            }
        },1000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        userCharGetHandler.removeCallbacksAndMessages("remove");
    }

    @Override
    public void getResponse(String response, int flag) {
        try{
            progressIndicator.DismissProgress();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            switch (flag){
                case 1:
                    JSONObject messageReceiveObj = new JSONObject(response);
                    if(messageReceiveObj.getBoolean("status")){
                        chatMessageModels.clear();
                        JSONArray responseArray = messageReceiveObj.getJSONArray("response");
                        for(int i=0;i<responseArray.length();i++){
                            JSONObject messageObj = responseArray.getJSONObject(i);
                            if(messageObj.getString("reciever_id")
                                    .equals(SharedPreferencesUtil.getInstance(getActivity()).getCustomerID())){
                                chatMessageModels.add(new ChatMessageModel(1,messageObj.getString("message")));
                            }else {
                                chatMessageModels.add(new ChatMessageModel(2,messageObj.getString("message")));
                            }
                        }

                        chatAdpater = new ChatAdpater(getActivity(),chatMessageModels);
                        userChat.setAdapter(chatAdpater);
                        userChat.scrollToPosition(chatMessageModels.size() - 1);
                        //userChat.smoothScrollToPosition(chatMessageModels.size()-1);

                    }else {
                        Toast.makeText(getActivity(), messageReceiveObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){

                    }else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
