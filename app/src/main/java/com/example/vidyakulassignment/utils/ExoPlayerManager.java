package com.example.vidyakulassignment.utils;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.vidyakulassignment.R;
import com.github.rubensousa.previewseekbar.PreviewBar;
import com.github.rubensousa.previewseekbar.PreviewLoader;
import com.github.rubensousa.previewseekbar.media3.PreviewTimeBar;

@OptIn(markerClass = UnstableApi.class)
public class ExoPlayerManager implements PreviewLoader, PreviewBar.OnScrubListener {

    private static final String VIDEO_PATH = "asset:///video.mp4";

    private PlayerView playerView;
    private ExoPlayer player;
    private PreviewTimeBar previewTimeBar;
    private ImageView imageView;
    private boolean resumeVideoOnPreviewStop;
    private Player.Listener eventListener = new Player.Listener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_READY && playWhenReady) {
                previewTimeBar.hidePreview();
            }
        }
    };

    public ExoPlayerManager(PlayerView playerView,
                            PreviewTimeBar previewTimeBar,
                            ImageView imageView) {
        this.playerView = playerView;
        this.imageView = imageView;
        this.previewTimeBar = previewTimeBar;
        this.previewTimeBar.addOnScrubListener(this);
        this.previewTimeBar.setPreviewLoader(this);
        this.resumeVideoOnPreviewStop = true;
    }

    public void onStart() {
        if (Util.SDK_INT > 23) {
            createPlayers();
        }
    }

    public void onResume() {
        if (Util.SDK_INT <= 23) {
            createPlayers();
        }
    }

    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayers();
        }
    }

    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayers();
        }
    }

    public void setResumeVideoOnPreviewStop(boolean resume) {
        this.resumeVideoOnPreviewStop = resume;
    }

    private void releasePlayers() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void createPlayers() {
        if (player != null) {
            player.release();
        }
        player = createPlayer();
        playerView.setPlayer(player);
        playerView.setControllerShowTimeoutMs(15000);
    }

    private ExoPlayer createPlayer() {
        ExoPlayer player = new ExoPlayer.Builder(playerView.getContext())
                .build();
        player.setPlayWhenReady(true);
        player.setMediaItem(new MediaItem.Builder()
                .setUri(Uri.parse("android.resource://com.example.vidyakulassignment/"+ R.raw.shivali_moments))
                .build());
        player.addListener(eventListener);
        player.prepare();
        return player;
    }

    @Override
    public void loadPreview(long currentPosition, long max) {
        if (player.isPlaying()) {
            player.setPlayWhenReady(false);
        }
        String thumbnailUrl = "android.resource://com.example.vidyakulassignment/"+ R.raw.shivali_moments;

        Glide.with(imageView)
                .load(thumbnailUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .transform(new GlideThumbnailTransformation(currentPosition))
                .into(imageView);
    }

    @Override
    public void onScrubStart(PreviewBar previewBar) {
        player.setPlayWhenReady(false);
    }

    @Override
    public void onScrubMove(PreviewBar previewBar, int progress, boolean fromUser) {

    }

    @Override
    public void onScrubStop(PreviewBar previewBar) {
        if (resumeVideoOnPreviewStop) {
            player.setPlayWhenReady(true);
        }
    }

}
