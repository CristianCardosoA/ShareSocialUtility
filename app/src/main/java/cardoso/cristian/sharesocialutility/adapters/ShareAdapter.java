package cardoso.cristian.sharesocialutility.adapters;

/**
 * Created by macbook on 22/10/16.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cardoso.cristian.sharesocialutility.R;
import cardoso.cristian.sharesocialutility.object.Share;

public class ShareAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Share> apps;
    public ShareAdapter(Context context, List<Share> apps) {
        layoutInflater = LayoutInflater.from(context);
        this.apps = apps;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.txvSocialNetwork);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imgSocialNetowork);
            viewHolder.textView.setGravity(Gravity.CENTER_VERTICAL);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Share app = apps.get(position);
        viewHolder.textView.setText(app.getAppComercialName());
        viewHolder.imageView.setImageResource(app.getImage());
        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
