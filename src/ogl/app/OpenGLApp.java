/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package ogl.app;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.awt.*;

/**
 * A simple framework for OpenGL applications.
 */
public final class OpenGLApp {

  /**
   * Create an OpenGL application with one window. The application behavior is
   * controlled by the <code>App</code> object.
   * 
   * @param title
   *          The string that is displayed in the title bar of the application
   *          window.
   * @param application
   *          The application object.
   */
  public OpenGLApp(String title, App application) {
    this(title, application, false);
  }

  /**
   * Create an OpenGL application with one window. The application behavior is
   * controlled by the <code>App</code> object.
   * 
   * @param title
   *          The string that is displayed in the title bar of the application
   *          window.
   * @param multisampling
   *          Multisampling is used if true. May not work on all platforms.
   * @param application
   *          The application object.
   */
  public OpenGLApp(String title, App application, boolean multisampling) {
    this.title = title;
    this.application = application;
    this.multisampling = multisampling
        && System.getProperty("os.name").equals("Mac OS X");
    
    System.out.println("LWJGL version " + Sys.getVersion() + " running on "
        + System.getProperty("os.name") + " version "
        + System.getProperty("os.version") + ".");
  }

  /**
   * Start the application. The window is opened and the main loop is entered.
   * This call does not return until the window is closed or an exception was
   * caught.
   */
  public void start() {
    try {
      Display.setDisplayMode(new DisplayMode(width, height));
      Display.setTitle(title);
      if (multisampling)
        Display.create(new PixelFormat().withSamples(8));
      else
        Display.create();
        
      // Sync buffer swap with vertical sync. Results in 60 fps on my Mac
      // Book.
      Display.setSwapInterval(1);
      Display.setVSyncEnabled(true);

      input = new Input();
      application.init();

      while (!Display.isCloseRequested()) {
        input.update();
        input.setWindowSize(width, height);
        application.simulate(time.elapsed(), input);
        application.display(width, height);
        Display.update();
      }
    } catch (LWJGLException e) {
      e.printStackTrace();
    } finally {
      Display.destroy();
    }
  }

  private int width = 600;
  private int height = 600;

  private StopWatch time = new StopWatch();
  private Input input;
  private App application;
  private String title;
  private boolean multisampling = false;
}
