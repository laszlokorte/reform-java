package reform.playground.main;

import reform.core.procedure.Procedure;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.data.sheet.Sheet;
import reform.evented.core.EventedProject;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2i;
import reform.naming.Name;

import javax.swing.*;

public final class Main
{
	public static void main(final String[] args)
	{
		initStyle();
		SwingUtilities.invokeLater(() -> {
			final Project project = new Project();
			final EventedProject eProject = new EventedProject(project);
			final IdentifierEmitter idEmitter = new IdentifierEmitter(100);

			final Picture pic = new Picture(idEmitter.emit(), new Name("Foo Picture"),
			                                new Vec2i(700, 400), new Sheet(),
			                                new Procedure(), new Sheet());
			eProject.addPicture(pic);

			final WindowBuilder windowBuilder = new WindowBuilder();

			windowBuilder.open(null, project, idEmitter, true);
		});

	}

	private static void initStyle()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		try
		{
			UIManager.getDefaults().put("SplitPane.border",
			                            BorderFactory.createEmptyBorder());
			UIManager.getDefaults().put("Button.border", BorderFactory.createEmptyBorder(3,4,3,4));

			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name",
			                   "Reform");

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final ClassNotFoundException e)
		{
			System.out.println("ClassNotFoundException: " + e.getMessage());
		} catch (final InstantiationException e)
		{
			System.out.println("InstantiationException: " + e.getMessage());
		} catch (final IllegalAccessException e)
		{
			System.out.println("IllegalAccessException: " + e.getMessage());
		} catch (final UnsupportedLookAndFeelException e)
		{
			System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
		}
	}
}
