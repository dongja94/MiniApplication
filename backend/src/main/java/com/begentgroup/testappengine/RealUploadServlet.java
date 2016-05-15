package com.begentgroup.testappengine;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Ref;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dongja94 on 2016-05-13.
 */
public class RealUploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("User");
        if (user != null) {
            ImageContent content = new ImageContent();
            content.content = req.getParameter("content");
            content.writer = Ref.create(user);
            Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            List<BlobKey> blobKeys = blobs.get("myFile");
            if (blobKeys != null && !blobKeys.isEmpty()) {
                content.imageUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/" + "displayimage?imageid=" +blobKeys.get(0).getKeyString();
//            resp.getWriter().print("key " + blobKeys.get(0).getKeyString());
//                blobstoreService.serve(blobKeys.get(0), resp);
            }
            DataManager.getInstance().addImageContent(content);
            Utility.responseSuccessMessage(resp, ImageContentResponse.convertContent(content));
            return;
        }
        Utility.responseErrorMessage(resp, "user not login");
    }
}
