package reform.evented.core;

import reform.data.sheet.Sheet;

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
