package ali.imanifard.raspberrycart.feature.main.product_detail_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ali.imanifard.raspberrycart.R;
import ali.imanifard.raspberrycart.data.Product.ProductDTO;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    //public List<ProductDTO> productDTOS = new ArrayList<>();
    private ProductDTO productDTO;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentsByDefault commentsByDefault = new CommentsByDefault();
        holder.bindComment(commentsByDefault, position);
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    public void setProducts(ProductDTO productDTO) {
        this.productDTO = productDTO;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCommentTitle, tvCommentDate, tvCommentAuthor, tvCommentContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentTitle = itemView.findViewById(R.id.tv_comment_title);
            tvCommentDate = itemView.findViewById(R.id.tv_comment_date);
            tvCommentAuthor = itemView.findViewById(R.id.tv_comment_author);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
        }

        public void bindComment(CommentsByDefault commentsByDefault, int position) {

            int[] commentTitles = commentsByDefault.getCommentTitle();
            int[] commentDates = commentsByDefault.getTvCommentDate();
            int[] commentAuthors = commentsByDefault.getTvCommentAuthor();
            int[] commentContents = commentsByDefault.getTvCommentContent();

            tvCommentTitle.setText(itemView.getResources().getString(commentTitles[position]));
            tvCommentDate.setText(itemView.getResources().getString(commentDates[position]));
            tvCommentAuthor.setText(itemView.getResources().getString(commentAuthors[position]));
            tvCommentContent.setText(itemView.getResources().getString(commentContents[position]));


        }

    }
}
