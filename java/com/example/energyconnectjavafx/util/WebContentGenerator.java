package com.example.energyconnectjavafx.util;

public class WebContentGenerator {

    public static String getFacebookUrl() {
        return "https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2FEnergyFMNaga&tabs=timeline&width=500&height=800&small_header=true&adapt_container_width=true&hide_cover=true&show_facepile=false";
    }

    public static String getYouTubeCarouselHtml(String channelId) {
        String playlistId = "UU" + channelId.substring(2);

        StringBuilder html = new StringBuilder();

        html.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <base href="https://www.youtube.com">
                <style>
                    body { margin: 0; padding: 15px; background: #F4F4F4; font-family: sans-serif; overflow-y: hidden; }
                    
                    .carousel-container {
                        display: flex;
                        gap: 15px;
                        overflow-x: auto;
                        padding-bottom: 10px;
                        white-space: nowrap;
                    }
                    .carousel-container::-webkit-scrollbar { height: 8px; }
                    .carousel-container::-webkit-scrollbar-thumb { background: #888; border-radius: 4px; }
                    
                    .video-card {
                        flex: 0 0 300px;
                        width: 300px;
                        height: 169px;
                        border-radius: 12px;
                        overflow: hidden;
                        background: #000;
                        box-shadow: 0 4px 10px rgba(0,0,0,0.2);
                        /* Fixes iframe flash issues */
                        transform: translateZ(0); 
                    }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <div class="carousel-container">
            """);

        for (int i = 1; i <= 10; i++) {
            // Embed the video using the CORRECT playlistId
            html.append(String.format(
                    "<div class='video-card'>" +
                            "<iframe src='https://www.youtube.com/embed?listType=playlist&list=%s&index=%d' " +
                            "sandbox='allow-scripts allow-same-origin allow-presentation' " +
                            "allowfullscreen></iframe>" +
                            "</div>",
                    playlistId, i
            ));
        }

        html.append("</div></body></html>");
        return html.toString();
    }
}
