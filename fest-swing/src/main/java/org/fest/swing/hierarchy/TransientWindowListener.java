/*
 * Created on Oct 31, 2007
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2007-2010 the original author or authors.
 */
package org.fest.swing.hierarchy;

import static javax.swing.SwingUtilities.invokeLater;
import static org.fest.swing.util.AWTEvents.*;

import java.awt.*;
import java.awt.event.AWTEventListener;

import org.fest.swing.annotation.RunsInEDT;

/**
 * Understands automatic filtering of auto-generated Swing dialogs.
 *
 * @author Alex Ruiz
 */
public final class TransientWindowListener implements AWTEventListener {

  private final WindowFilter filter;

  TransientWindowListener(WindowFilter filter) {
    this.filter = filter;
  }

  /** {@inheritDoc} */
  @RunsInEDT
  public void eventDispatched(AWTEvent e) {
    if (windowOpened(e) || windowShown(e)) {
      filter(sourceOf(e));
      return;
    }
    if (windowClosed(e)) {
      final Window w = sourceOf(e);
      // *Any* window disposal should result in the window being ignored, at least until it is again displayed.
      if (filter.isIgnored(w)) return;
      filter.implicitlyIgnore(w);
      // Filter this window only *after* any handlers for this event have finished.
      invokeLater(new IgnoreWindowTask(w, filter));
    }
  }

  private Window sourceOf(AWTEvent e) {
    return (Window) e.getSource();
  }

  private void filter(Window w) {
    if (filter.isImplicitlyIgnored(w)) {
      filter.recognize(w);
      return;
    }
    // Catch new sub-windows of filtered windows (i.e. dialogs generated by a test harness UI).
    filterIfParentIsFiltered(w);
  }

  private void filterIfParentIsFiltered(Window w) {
    if (!filter.isIgnored(w.getParent())) return;
    filter.ignore(w);
  }
}
