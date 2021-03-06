package yesteam.adoptame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdoptaAdapter extends RecyclerView.Adapter<AdoptaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemPet> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public ImageView imgPet;

        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.txtName);
            imgPet = (ImageView) v.findViewById(R.id.imgPet);
        }
    }

    public AdoptaAdapter(Context context, ArrayList<ItemPet> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adopta_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        ItemPet pet = items.get(position);
        vh.txtName.setText(pet.getNombre());
        Picasso.with(context).cancelRequest(vh.imgPet);
        Picasso.with(context).load(pet.getFoto()).placeholder(R.drawable.no_photo).into(vh.imgPet);
    }

    @Override
    public int getItemCount() {
        if (items == null)
        {
            return 0;
        } else {
            return items.size();
        }
    }

    public ItemPet getItem(int pos) {
        return items.get(pos);
    }
}
