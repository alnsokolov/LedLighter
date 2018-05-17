package ru.hawk_inc.ledlighter.Help;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ru.hawk_inc.ledlighter.R;

/**
 * Created by Admin on 12/8/2017.
 */

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ViewHolder> {

    private ArrayList<BluetoothDevice> devices = null;
    private ScanAdapter mConnectAdapter;

    public ScanAdapter(ArrayList<BluetoothDevice> devices){
        this.devices = devices;
        mConnectAdapter = this;
    }

    @Override
    public ScanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_scan, parent, false);

        return new ScanAdapter.ViewHolder(v);
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
        private BluetoothDevice device;

        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            text = (TextView)itemView.findViewById(R.id.adapter_scan_text);
            subText = (TextView)itemView.findViewById(R.id.adapter_scan_subtext);
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
        }
    }
}
