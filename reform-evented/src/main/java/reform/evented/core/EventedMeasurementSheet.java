package reform.evented.core;

import reform.data.sheet.Sheet;

public class EventedMeasurementSheet extends EventedSheetBase implements EventedSheet
{

	@Override
	Sheet getSheet()
	{
		return _evtPicture.getMeasurementSheet();
	}

	public EventedMeasurementSheet(final EventedPicture evtPicture)
	{
		super(evtPicture);
	}


}
