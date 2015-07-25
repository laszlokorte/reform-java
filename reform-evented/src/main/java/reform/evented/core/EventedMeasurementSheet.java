package reform.evented.core;

import reform.data.sheet.Sheet;

public class EventedMeasurementSheet extends EventedSheetBase implements EventedSheet
{

	public EventedMeasurementSheet(final EventedPicture evtPicture)
	{
		super(evtPicture);
	}

	@Override
	Sheet getSheet()
	{
		return _evtPicture.getMeasurementSheet();
	}


}
