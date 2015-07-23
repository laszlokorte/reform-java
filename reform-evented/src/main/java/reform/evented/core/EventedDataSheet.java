package reform.evented.core;

import reform.data.sheet.Definition;
import reform.data.sheet.Sheet;
import reform.data.sheet.expression.Expression;
import reform.identity.Identifier;

import java.util.ArrayList;

public class EventedDataSheet extends EventedSheetBase implements EventedSheet
{

	@Override
	Sheet getSheet()
	{
		return _evtPicture.getDataSheet();
	}

	public EventedDataSheet(final EventedPicture evtPicture)
	{
		super(evtPicture);
	}


}
