package com.javagrunt.service.opencvcapture.capture;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
class CaptureController {
    
    @ResponseBody
    @RequestMapping(value = "/capture", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] live() throws IOException {
        //This will grab an image from the default webcam (deviceNumber 0)
        Mat grabbedImage;
        try (FrameGrabber grabber = new OpenCVFrameGrabber(0)) {
            grabber.start();
            try (OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat()) {
                grabbedImage = converter.convert(grabber.grab());
            }
            grabber.stop();
        }

        BufferedImage image = Java2DFrameUtils.toBufferedImage(grabbedImage);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "jpg", os);
        return os.toByteArray();
    }
    
}
