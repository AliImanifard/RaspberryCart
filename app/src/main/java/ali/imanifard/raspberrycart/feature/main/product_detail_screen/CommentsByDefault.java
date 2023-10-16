package ali.imanifard.raspberrycart.feature.main.product_detail_screen;

import androidx.annotation.StringRes;

import ali.imanifard.raspberrycart.R;

public class CommentsByDefault {
    @StringRes
    private final int[] CommentTitle = new int[]{R.string.comment_title_1, R.string.comment_title_2, R.string.comment_title_3};
    @StringRes
    private final int[] tvCommentDate = new int[]{R.string.comment_date_1, R.string.comment_date_2, R.string.comment_date_3};
    @StringRes
    private final int[] tvCommentAuthor = new int[]{R.string.comment_author_1, R.string.comment_author_2, R.string.comment_author_3};
    @StringRes
    private final int[] tvCommentContent = new int[]{R.string.comment_content_1, R.string.comment_content_2, R.string.comment_content_3};


    public int[] getCommentTitle() {
        return CommentTitle;
    }

    public int[] getTvCommentDate() {
        return tvCommentDate;
    }

    public int[] getTvCommentAuthor() {
        return tvCommentAuthor;
    }

    public int[] getTvCommentContent() {
        return tvCommentContent;
    }
}
