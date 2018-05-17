package ru.hawk_inc.ledlighter.Help;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.hawk_inc.ledlighter.R;

/**
 * Created by Admin on 12/6/2017.
 */

public class ConnectAdapter extends RecyclerView.Adapter<ConnectAdapter.ViewHolder> {

    private ArrayList<BluetoothDevice> devices = null;
    private ConnectAdapter mConnectAdapter;

    public ConnectAdapter(ArrayList<BluetoothDevice> devices){
        this.devices = devices;
        mConnectAdapter = this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_connect, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        holder.setDevice(device, position);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text;
        private TextView subText;
        private ImageButton delete;
        private BluetoothDevice device;

        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            text = (TextView)itemView.findViewById(R.id.adapter_connect_text);
            subText = (TextView)itemView.findViewById(R.id.adapter_connect_subtext);
            delete = (ImageButton)itemView.findViewById(R.id.adapter_delete);
        }

        public void setDevice(final BluetoothDevice device, final int position){
            this.device = device;
            String name = device.getName();

            if(name != null) {
                text.setText(device.getName());
                subText.setText(device.getAddress());
            }
            else
                text.setText(device.getAddress());

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Write some code for connecting to device
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    devices.remove(device);
                    mConnectAdapter.notifyItemRemoved(position);
                    mConnectAdapter.notifyItemRangeChanged(position, 1);
                }
            });
        }
    }
}
