package com.example.inventoryapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.example.inventoryapp.data.ArticleModel;

import java.util.ArrayList;

public class ProductCursorAdapter extends RecyclerView.Adapter<ProductCursorAdapter.ViewHolder> {

    private ArrayList<ArticleModel> mList;
    private OnItemClickListener listener;

    public ProductCursorAdapter(ArrayList<ArticleModel> mList, OnItemClickListener listener) {
        this.mList = mList;
        this.listener = listener;
    }


    /*@Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // inflate list view item
        return LayoutInflater.from(context).inflate(R.layout.item_recycler_view,
                viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // individual views to modify
        TextView nameTextView = view.findViewById(R.id.product_name_tv);
        ConstraintLayout rootView = view.findViewById(R.id.product_root_view);
        TextView priceTextView = view.findViewById(R.id.product_price_tv);
        TextView availableTextView = view.findViewById(R.id.availability_tv);
        ImageView productImageView = view.findViewById(R.id.product_image_view);
        final Button saleButton = view.findViewById(R.id.button);

        // columns of product
        int nameColumnIndex =
                cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex =
                cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int availabilityColumnIndex =
                cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_AVAILABILITY);
        int imageColumnIndex =
                cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE);

        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        int productAvailability = cursor.getInt(availabilityColumnIndex);
        String productImage = cursor.getString(imageColumnIndex);

        // update views with data
        nameTextView.setText(productName);
        priceTextView.setText("$" + productPrice);
        availableTextView.setText(Integer.toString(productAvailability));
        if (productImage != null) {
            Glide.with(context).load(Uri.parse(productImage)).into(productImageView);
        }


        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "button clicked: " , Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView prductName;
        TextView productPrice;
        TextView phone;
        TextView availability;
        Button sale;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            image = itemView.findViewById(R.id.product_image_view);
            prductName = itemView.findViewById(R.id.product_name_tv);
            productPrice = itemView.findViewById(R.id.product_price_tv);
            phone = itemView.findViewById(R.id.in_stock_tv);
            availability = itemView.findViewById(R.id.availability_tv);
            sale = itemView.findViewById(R.id.button);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(Uri.parse(mList.get(position).getImage())).into(holder.image);
        holder.prductName.setText(mList.get(position).getPrductName());
        holder.productPrice.setText(mList.get(position).getProductPrice());
        holder.phone.setText(mList.get(position).getPhone());
        holder.availability.setText(mList.get(position).getAvailability());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Integer position);
    }

}
