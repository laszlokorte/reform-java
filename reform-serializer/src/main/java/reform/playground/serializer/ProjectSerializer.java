package reform.playground.serializer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;
import reform.core.forms.*;
import reform.core.forms.anchors.Anchor;
import reform.core.forms.relations.*;
import reform.core.graphics.DrawingType;
import reform.core.procedure.Procedure;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.InstructionGroup;
import reform.core.procedure.instructions.NullInstruction;
import reform.core.procedure.instructions.blocks.ForLoopInstruction;
import reform.core.procedure.instructions.blocks.IfConditionInstruction;
import reform.core.procedure.instructions.single.*;
import reform.core.project.DataSet;
import reform.core.project.Picture;
import reform.core.project.Project;
import reform.core.runtime.relations.*;
import reform.core.runtime.relations.Direction.CartesianDirection;
import reform.core.runtime.relations.InitialDestination.Alignment;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2;
import reform.math.Vec2i;
import reform.naming.Name;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProjectSerializer
{
	private static final int VERSION = 1;

	private final IdentifierEmitter _idEmitter;
	private final Set<Identifier<? extends Form>> _formIds = new HashSet<>();

	public ProjectSerializer(final IdentifierEmitter idEmitter)
	{
		_idEmitter = idEmitter;
	}

	public Project read(final JSONObject object)
	{
		if (object.getInt("version") != VERSION)
		{
			throw new SerializationError("Invalid Version");
		}

		cleanup();

		return readProject(object.getJSONObject("project"));
	}

	private void cleanup()
	{
		_formIds.clear();
	}

	private Project readProject(final JSONObject jsonObject)
	{
		final Project project = new Project();

		for (final Picture p : readPictures(jsonObject.getJSONArray("pictures")))
		{
			project.addPicture(p);
		}

		return project;
	}

	private Iterable<Picture> readPictures(final JSONArray jsonArray)
	{
		final ArrayList<Picture> result = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++)
		{
			result.add(readPicture(jsonArray.getJSONObject(i)));
		}

		return result;
	}

	private Picture readPicture(final JSONObject object)
	{
		final Identifier<? extends Picture> id = readId(object, "id");
		final Name name = readName(object);
		final Vec2i size = readIntegerVector(object.getJSONObject("size"));
		final Procedure procedure = readProcedure(object.getJSONObject("procedure"));

		registerPictureId(id);

		return new Picture(id, name, size, new DataSet(), procedure);

	}

	private Procedure readProcedure(final JSONObject jsonObject)
	{
		final Procedure procedure = new Procedure();

		readInstructionsInto(procedure, procedure.getRoot(), jsonObject.getJSONArray("instructions"));

		return procedure;
	}

	private void readInstructionsInto(final Procedure procedure, final InstructionGroup root, final JSONArray
			jsonArray)
	{
		boolean prevWasGroup = false;
		for (int i = 0; i < jsonArray.length(); i++)
		{
			final Instruction instruction = readInstruction(procedure, jsonArray.getJSONObject(i));
			if (prevWasGroup && !(instruction instanceof NullInstruction))
			{
				final NullInstruction n = new NullInstruction();
				root.append(n);
			}
			root.append(instruction);

			prevWasGroup = instruction instanceof InstructionGroup;

		}

		if (prevWasGroup)
		{
			final NullInstruction n = new NullInstruction();
			root.append(n);
		}
	}

	private Instruction readInstruction(final Procedure procedure, final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");

		if (type.equals(NullInstruction.class.getSimpleName()))
		{
			return readNullInstruction();
		}
		else if (type.equals(CreateFormInstruction.class.getSimpleName()))
		{
			return readCreateFormInstruction(jsonObject);
		}
		else if (type.equals(MorphInstruction.class.getSimpleName()))
		{
			return readMorphInstruction(jsonObject);
		}
		else if (type.equals(TranslateInstruction.class.getSimpleName()))
		{
			return readTranslateInstruction(jsonObject);
		}
		else if (type.equals(ScaleInstruction.class.getSimpleName()))
		{
			return readScaleInstruction(jsonObject);
		}
		else if (type.equals(RotateInstruction.class.getSimpleName()))
		{
			return readRotateInstruction(jsonObject);
		}
		else if (type.equals(ForLoopInstruction.class.getSimpleName()))
		{
			return readForLoopInstruction(procedure, jsonObject);
		}
		else if (type.equals(IfConditionInstruction.class.getSimpleName()))
		{
			return readIfConditionInstruction(procedure, jsonObject);
		}
		else if (type.equals(ErrorInstruction.class.getSimpleName()))
		{
			return readErrorInstruction(jsonObject);
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private Instruction readErrorInstruction(final JSONObject jsonObject)
	{
		return new ErrorInstruction();
	}

	private NullInstruction readNullInstruction()
	{
		return new NullInstruction();

	}

	private CreateFormInstruction readCreateFormInstruction(final JSONObject object)
	{
		final Form form = readForm(object.getJSONObject("form"));
		final InitialDestination destination = readDestination(object.getJSONObject("destination"));

		registerFormId(form.getId());

		return new CreateFormInstruction(form, destination);
	}

	private void registerFormId(final Identifier<? extends Form> id)
	{
		_idEmitter.markUsed(id);
		if (!_formIds.add(id))
		{
			throw new SerializationError("Duplicate form id");
		}
	}

	private void registerPictureId(final Identifier<? extends Picture> id)
	{
		_idEmitter.markUsed(id);
	}

	private InitialDestination readDestination(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");

		if (type.equals(RelativeDynamicSizeDestination.class.getSimpleName()))
		{
			return readRelativeDynamicSizeDestination(jsonObject);
		}
		else if (type.equals(RelativeFixSizeDestination.class.getSimpleName()))
		{
			return readRelativeFixSizeDestination(jsonObject);
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private RelativeFixSizeDestination readRelativeFixSizeDestination(final JSONObject jsonObject)
	{
		final RelativeFixSizeDestination d = new RelativeFixSizeDestination(readPoint(jsonObject.getJSONObject("ref"))
				, readVector(jsonObject.getJSONObject("delta")));
		d.setAlignment(readAlignment(jsonObject.getJSONObject("alignment")));
		return d;
	}

	private InitialDestination readRelativeDynamicSizeDestination(final JSONObject jsonObject)
	{
		final RelativeDynamicSizeDestination d = new RelativeDynamicSizeDestination(readPoint(jsonObject.getJSONObject
				("refA")), readPoint(jsonObject.getJSONObject("refB")));
		d.setAlignment(readAlignment(jsonObject.getJSONObject("alignment")));
		d.setDirection(readDirection(jsonObject.getJSONObject("direction")));
		return d;
	}

	private Direction readDirection(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");
		if (type.equals(CartesianDirection.class.getSimpleName()))
		{
			return readCartesianDirection(jsonObject);
		}
		else if (type.equals(ProportionalDirection.class.getSimpleName()))
		{
			return readProportionalDirection(jsonObject);
		}
		else if (type.equals(FreeDirection.Free.name()))
		{
			return FreeDirection.Free;
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private Direction readProportionalDirection(final JSONObject jsonObject)
	{
		return new ProportionalDirection(jsonObject.getDouble("proportion"));
	}

	private Direction readCartesianDirection(final JSONObject jsonObject)
	{
		final String value = jsonObject.getString("value");
		if (value.equals(CartesianDirection.Horizontal.name()))
		{
			return CartesianDirection.Horizontal;
		}
		else if (value.equals(CartesianDirection.Vertical.name()))
		{
			return CartesianDirection.CartesianDirection.Vertical;
		}
		else
		{
			throw new SerializationError(value);
		}
	}

	private Alignment readAlignment(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");
		if (type.equals(Alignment.Center.name()))
		{
			return Alignment.Center;
		}
		else if (type.equals(Alignment.Leading.name()))
		{
			return Alignment.Leading;
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private Vec2 readVector(final JSONObject jsonObject)
	{
		return new Vec2(jsonObject.getDouble("x"), jsonObject.getDouble("y"));
	}

	private Form readForm(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("form");

		final Form form;

		if (type.equals(LineForm.class.getSimpleName()))
		{
			form = LineForm.construct(readId(jsonObject, "id"), readName(jsonObject));
		}
		else if (type.equals(RectangleForm.class.getSimpleName()))
		{
			form = RectangleForm.construct(readId(jsonObject, "id"), readName(jsonObject));
		}
		else if (type.equals(CircleForm.class.getSimpleName()))
		{
			form = CircleForm.construct(readId(jsonObject, "id"), readName(jsonObject));
		}
		else if (type.equals(ArcForm.class.getSimpleName()))
		{
			form = ArcForm.construct(readId(jsonObject, "id"), readName(jsonObject));
		}
		else if (type.equals(PieForm.class.getSimpleName()))
		{
			form = PieForm.construct(readId(jsonObject, "id"), readName(jsonObject));
		}
		else if (type.equals(PictureForm.class.getSimpleName()))
		{
			form = PictureForm.construct(readId(jsonObject, "id"), readName(jsonObject));
		}
		else
		{
			throw new SerializationError(type);
		}

		form.setType(readDrawingType(jsonObject.getString("type")));

		return form;
	}

	private DrawingType readDrawingType(final String type)
	{
		if (type.equals(DrawingType.Draw.name()))
		{
			return DrawingType.Draw;
		}
		else if (type.equals(DrawingType.Guide.name()))
		{
			return DrawingType.Guide;
		}
		else if (type.equals(DrawingType.Mask.name()))
		{
			return DrawingType.Mask;
		}
		else if (type.equals(DrawingType.None.name()))
		{
			return DrawingType.None;
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private Name readName(final JSONObject jsonObject)
	{
		return new Name(jsonObject.getString("name"));
	}

	private MorphInstruction readMorphInstruction(final JSONObject object)
	{
		final Identifier<? extends Form> formId = readId(object, "form");
		final Identifier<? extends Anchor> anchorId = readId(object, "anchor");
		final TranslationDistance distance = readDistance(object.getJSONObject("distance"));
		return new MorphInstruction(formId, anchorId, distance);
	}

	private TranslationDistance readDistance(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");

		if (type.equals(ConstantDistance.class.getSimpleName()))
		{
			return readConstantDistance(jsonObject);
		}
		else if (type.equals(RelativeDistance.class.getSimpleName()))
		{
			return readRelativeDistance(jsonObject);
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private TranslationDistance readRelativeDistance(final JSONObject jsonObject)
	{
		final RelativeDistance d = new RelativeDistance(readPoint(jsonObject.getJSONObject("refA")), readPoint
				(jsonObject.getJSONObject("refB")));

		d.setDirection(readDirection(jsonObject.getJSONObject("direction")));

		return d;
	}

	private TranslationDistance readConstantDistance(final JSONObject jsonObject)
	{
		return new ConstantDistance(readVector(jsonObject.getJSONObject("delta")));
	}

	private <T> Identifier<T> readId(final JSONObject object, final String key)
	{
		return new Identifier<>(object.getInt(key));
	}

	private TranslateInstruction readTranslateInstruction(final JSONObject object)
	{
		return new TranslateInstruction(readId(object, "form"), readDistance(object.getJSONObject("distance")));
	}

	private ScaleInstruction readScaleInstruction(final JSONObject object)
	{
		return new ScaleInstruction(readId(object, "form"), readScaleFactor(object.getJSONObject("factor")), readPoint
				(object.getJSONObject("fixpoint")));
	}

	private ReferencePoint readPoint(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");

		if (type.equals(ForeignFormsPoint.class.getSimpleName()))
		{
			return readForeignFormsPoint(jsonObject);
		}
		else if (type.equals(ConstantPoint.class.getSimpleName()))
		{
			return readConstantPoint(jsonObject);
		}
		else if (type.equals(IntersectionPoint.class.getSimpleName()))
		{
			return readIntersectionPoint(jsonObject);
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private ReferencePoint readConstantPoint(final JSONObject jsonObject)
	{
		return new ConstantPoint(readVector(jsonObject.getJSONObject("value")));
	}

	private ReferencePoint readIntersectionPoint(final JSONObject jsonObject)
	{
		return new IntersectionPoint(jsonObject.getInt("index"), readId(jsonObject, "formAId"), readId(jsonObject,
				"formBId"));
	}

	private ReferencePoint readForeignFormsPoint(final JSONObject jsonObject)
	{
		return new ForeignFormsPoint(readId(jsonObject, "formId"), readId(jsonObject, "pointId"));
	}

	private ScaleFactor readScaleFactor(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");

		if (type.equals(ConstantScaleFactor.class.getSimpleName()))
		{
			return readConstantScaleFactor(jsonObject);
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private ScaleFactor readConstantScaleFactor(final JSONObject jsonObject)
	{
		return new ConstantScaleFactor(jsonObject.getDouble("value"));
	}

	private RotationAngle readConstantRotationAngle(final JSONObject jsonObject)
	{
		return new ConstantAngle(jsonObject.getDouble("value"));
	}

	private RotateInstruction readRotateInstruction(final JSONObject object)
	{
		return new RotateInstruction(readId(object, "form"), readRotationAngle(object.getJSONObject("angle")),
				readPoint(object.getJSONObject("fixpoint")));
	}

	private RotationAngle readRotationAngle(final JSONObject jsonObject)
	{
		final String type = jsonObject.getString("type");

		if (type.equals(ConstantAngle.class.getSimpleName()))
		{
			return readConstantRotationAngle(jsonObject);
		}
		else
		{
			throw new SerializationError(type);
		}
	}

	private ForLoopInstruction readForLoopInstruction(final Procedure procedure, final JSONObject object)
	{
		final ForLoopInstruction loop = new ForLoopInstruction(object.getInt("times"));

		readInstructionsInto(procedure, loop, object.getJSONArray("children"));

		return loop;
	}

	private IfConditionInstruction readIfConditionInstruction(final Procedure procedure, final JSONObject object)
	{
		final IfConditionInstruction condition = new IfConditionInstruction();

		condition.setCondition(object.getBoolean("condition"));
		readInstructionsInto(procedure, condition, object.getJSONArray("children"));

		return condition;
	}

	private Vec2i readIntegerVector(final JSONObject jsonObject)
	{
		return new Vec2i(jsonObject.getInt("x"), jsonObject.getInt("y"));
	}

	public void write(final JSONWriter writer, final Project project)
	{
		// @formatter:off
		writer.object();
		writer.key("version");
		writer.value(VERSION);
		writer.key("project");

		writer.object();
		writer.key("pictures");

		writer.array();


		final FastIterable<Identifier<? extends Picture>> pictures = project.getPictures();
		for (int i = 0; i < pictures.size(); i++)
		{
			final Identifier<? extends Picture> id = pictures.get(i);
			writePicture(writer, project.getPicture(id));
		}

		writer.endArray();

		writer.endObject();

		writer.endObject();
		// @formatter:on

	}

	private void writePicture(final JSONWriter writer, final Picture picture)
	{

		writer.object();
		writer.key("id");
		writer.value(Identifier.getValue(picture.getId()));
		writer.key("name");
		writer.value(picture.getName().getValue());
		writer.key("size");
		writeVectorI(writer, picture.getSize());

		writer.key("procedure");
		writeProcedure(writer, picture.getProcedure());

		writer.endObject();
	}

	private void writeVectorI(final JSONWriter writer, final Vec2i size)
	{
		writer.object();
		writer.key("x");
		writer.value(size.x);
		writer.key("y");
		writer.value(size.y);
		writer.endObject();
	}

	private void writeProcedure(final JSONWriter writer, final Procedure procedure)
	{
		writer.object();
		writer.key("instructions");
		writeGroupChildren(writer, procedure.getRoot());
		writer.endObject();
	}

	private void writeGroupChildren(final JSONWriter writer, final InstructionGroup root)
	{
		writer.array();
		for (final Instruction instruction : root)
		{
			writeInstruction(writer, instruction);
		}

		writer.endArray();

	}

	private void writeInstruction(final JSONWriter writer, final Instruction instruction)
	{
		final Class<? extends Instruction> cls = instruction.getClass();

		if (cls == NullInstruction.class)
		{
			return;
		}

		writer.object();
		writer.key("type");
		writer.value(cls.getSimpleName());

		if (cls == CreateFormInstruction.class)
		{
			writeCreateFormInstruction(writer, (CreateFormInstruction) instruction);
		}
		else if (cls == CreateGroupInstruction.class)
		{

		}
		else if (cls == ErrorInstruction.class)
		{

		}
		else if (cls == MorphInstruction.class)
		{
			writeMorphInstruction(writer, (MorphInstruction) instruction);
		}
		else if (cls == RotateInstruction.class)
		{
			writeRotateInstruction(writer, (RotateInstruction) instruction);

		}
		else if (cls == ScaleInstruction.class)
		{
			writeScaleInstruction(writer, (ScaleInstruction) instruction);

		}
		else if (cls == TranslateInstruction.class)
		{
			writeTranslateInstruction(writer, (TranslateInstruction) instruction);

		}
		else if (cls == ForLoopInstruction.class)
		{
			writeForLoopInstruction(writer, (ForLoopInstruction) instruction);

		}
		else if (cls == IfConditionInstruction.class)
		{
			writeIfConditionInstruction(writer, (IfConditionInstruction) instruction);

		}
		else
		{
			throw new SerializationError(cls.getName());
		}

		if (instruction instanceof InstructionGroup)
		{
			final InstructionGroup group = (InstructionGroup) instruction;
			writer.key("children");
			writeGroupChildren(writer, group);
		}
		writer.endObject();
	}

	private void writeIfConditionInstruction(final JSONWriter writer, final IfConditionInstruction instruction)
	{
		writer.key("condition");
		writer.value(instruction.getCondition());
	}

	private void writeForLoopInstruction(final JSONWriter writer, final ForLoopInstruction instruction)
	{
		writer.key("times");
		writer.value(instruction.getTimes());
	}

	private void writeTranslateInstruction(final JSONWriter writer, final TranslateInstruction instruction)
	{
		writer.key("form");
		writeId(writer, instruction.getFormId());
		writer.key("distance");
		writeTranslationDistance(writer, instruction.getDistance());
	}

	private void writeScaleInstruction(final JSONWriter writer, final ScaleInstruction instruction)
	{
		writer.key("form");
		writeId(writer, instruction.getFormId());
		writer.key("factor");
		writeScaleFactor(writer, instruction.getFactor());
		writer.key("fixpoint");
		writePoint(writer, instruction.getFixPoint());
	}

	private void writeRotateInstruction(final JSONWriter writer, final RotateInstruction instruction)
	{
		writer.key("form");
		writeId(writer, instruction.getFormId());
		writer.key("angle");
		writeRotationAngle(writer, instruction.getAngle());
		writer.key("fixpoint");
		writePoint(writer, instruction.getFixPoint());
	}

	private void writeMorphInstruction(final JSONWriter writer, final MorphInstruction instruction)
	{
		writer.key("form");
		writeId(writer, instruction.getFormId());
		writer.key("anchor");
		writeId(writer, instruction.getAnchorId());
		writer.key("distance");
		writeTranslationDistance(writer, instruction.getDistance());

	}

	private void writeCreateFormInstruction(final JSONWriter writer, final CreateFormInstruction instruction)
	{
		writer.key("form");
		writeForm(writer, instruction.getForm());
		writer.key("destination");
		writeDestination(writer, instruction.getDestination());
	}

	private void writeRotationAngle(final JSONWriter writer, final RotationAngle angle)
	{
		final Class<? extends RotationAngle> cls = angle.getClass();

		writer.object();
		writer.key("type");
		writer.value(cls.getSimpleName());
		if (cls == ConstantAngle.class)
		{
			writeConstantAngle(writer, (ConstantAngle) angle);
		}
		else
		{
			throw new SerializationError(cls.getSimpleName());
		}
		writer.endObject();
	}

	private void writeConstantAngle(final JSONWriter writer, final ConstantAngle angle)
	{
		writer.key("value");
		writer.value(angle.getValue());
	}

	private void writeScaleFactor(final JSONWriter writer, final ScaleFactor factor)
	{
		final Class<? extends ScaleFactor> cls = factor.getClass();

		writer.object();
		writer.key("type");
		writer.value(cls.getSimpleName());
		if (cls == ConstantScaleFactor.class)
		{
			writeConstantScaleFactor(writer, (ConstantScaleFactor) factor);
		}
		else
		{
			throw new SerializationError(cls.getSimpleName());
		}
		writer.endObject();
	}

	private void writeConstantScaleFactor(final JSONWriter writer, final ConstantScaleFactor factor)
	{
		writer.key("value");
		writer.value(factor.getValue());
	}

	private void writeTranslationDistance(final JSONWriter writer, final TranslationDistance distance)
	{
		final Class<? extends TranslationDistance> cls = distance.getClass();

		writer.object();
		writer.key("type");
		writer.value(cls.getSimpleName());

		if (cls == ConstantDistance.class)
		{
			writeConstantDistance(writer, (ConstantDistance) distance);
		}
		else if (cls == RelativeDistance.class)
		{
			writeRelativeDistance(writer, (RelativeDistance) distance);
		}
		else
		{
			throw new SerializationError(cls.getSimpleName());
		}

		writer.endObject();
	}

	private void writeRelativeDistance(final JSONWriter writer, final RelativeDistance distance)
	{
		writer.key("refA");
		writePoint(writer, distance.getReferenceA());
		writer.key("refB");
		writePoint(writer, distance.getReferenceB());
		writer.key("direction");
		writeDirection(writer, distance.getDirection());
	}

	private void writeConstantDistance(final JSONWriter writer, final ConstantDistance distance)
	{
		writer.key("delta");
		writeVector(writer, distance.getDelta());
	}

	private void writeDestination(final JSONWriter writer, final InitialDestination destination)
	{
		final Class<? extends InitialDestination> cls = destination.getClass();
		writer.object();
		writer.key("type");
		writer.value(cls.getSimpleName());

		if (cls == RelativeDynamicSizeDestination.class)
		{
			writeRelativeDynamicSizeDestination(writer, (RelativeDynamicSizeDestination) destination);

		}
		else if (cls == RelativeFixSizeDestination.class)
		{
			writeRelativeFixSizeDestination(writer, (RelativeFixSizeDestination) destination);

		}
		else
		{
			throw new SerializationError(cls.getName());
		}

		writer.endObject();
	}

	private void writeRelativeFixSizeDestination(final JSONWriter writer, final RelativeFixSizeDestination destination)
	{
		final ReferencePoint ref = destination.getReference();
		final Vec2 delta = destination.getDelta();
		final Alignment alignment = destination.getAlignment();

		writer.key("ref");
		writePoint(writer, ref);

		writer.key("delta");
		writeVector(writer, delta);

		writer.key("alignment");
		writeAlignment(writer, alignment);
	}

	private void writeRelativeDynamicSizeDestination(final JSONWriter writer, final RelativeDynamicSizeDestination
			destination)
	{
		final ReferencePoint refA = destination.getReferenceA();
		final ReferencePoint refB = destination.getReferenceB();
		final Direction direction = destination.getDirection();
		final Alignment alignment = destination.getAlignment();
		writer.key("refA");
		writePoint(writer, refA);
		writer.key("refB");
		writePoint(writer, refB);
		writer.key("direction");
		writeDirection(writer, direction);
		writer.key("alignment");
		writeAlignment(writer, alignment);
	}

	private void writeAlignment(final JSONWriter writer, final Alignment alignment)
	{
		writer.object();
		writer.key("type");
		writer.value(alignment.name());
		writer.endObject();
	}

	private void writeDirection(final JSONWriter writer, final Direction direction)
	{
		final Class<? extends Direction> cls = direction.getClass();
		writer.object();
		if (direction instanceof CartesianDirection)
		{
			writer.key("type");
			writer.value(CartesianDirection.class.getSimpleName());
			writeCartesianDirection(writer, (CartesianDirection) direction);
		}
		else if (cls == ProportionalDirection.class)
		{
			writer.key("type");
			writer.value(cls.getSimpleName());
			writeProportionalDirection(writer, (ProportionalDirection) direction);
		}
		else if (direction == FreeDirection.Free)
		{
			writer.key("type");
			writer.value(FreeDirection.Free.name());
		}
		else
		{
			throw new SerializationError(cls.getName());
		}
		writer.endObject();
	}

	private void writeCartesianDirection(final JSONWriter writer, final CartesianDirection direction)
	{
		writer.key("value");
		writer.value(direction.name());
	}

	private void writeProportionalDirection(final JSONWriter writer, final ProportionalDirection direction)
	{
		writer.key("proportion");
		writer.value(direction.getProportion());
	}

	private void writePoint(final JSONWriter writer, final ReferencePoint ref)
	{
		final Class<? extends ReferencePoint> cls = ref.getClass();

		writer.object();
		writer.key("type");
		writer.value(cls.getSimpleName());

		if (cls == ForeignFormsPoint.class)
		{
			writeForeignFormsPoint(writer, (ForeignFormsPoint) ref);
		}
		else if (cls == ConstantPoint.class)
		{
			writeConstantPoint(writer, (ConstantPoint) ref);
		}
		else if (cls == IntersectionPoint.class)
		{
			writeIntersectionPoint(writer, (IntersectionPoint) ref);
		}
		else
		{
			throw new SerializationError(cls.getName());
		}

		writer.endObject();
	}

	private void writeConstantPoint(final JSONWriter writer, final ConstantPoint point)
	{
		writer.key("value");
		writeVector(writer, point.getValue());
	}

	private void writeIntersectionPoint(final JSONWriter writer, final IntersectionPoint point)
	{
		writer.key("index");
		writer.value(point.getIndex());
		writer.key("formAId");
		writeId(writer, point.getFormAId());
		writer.key("formBId");
		writeId(writer, point.getFormBId());
	}

	private void writeForeignFormsPoint(final JSONWriter writer, final ForeignFormsPoint p)
	{
		writer.key("formId");
		writeId(writer, p.getFormId());
		writer.key("pointId");
		writeId(writer, p.getPointId());
	}

	private void writeId(final JSONWriter writer, final Identifier<?> formId)
	{
		writer.value(Identifier.getValue(formId));
	}

	private void writeVector(final JSONWriter writer, final Vec2 vec)
	{
		writer.object();
		writer.key("x");
		writer.value(vec.x);
		writer.key("y");
		writer.value(vec.y);
		writer.endObject();
	}

	private void writeForm(final JSONWriter writer, final Form form)
	{
		writer.object();
		writer.key("id");
		writeId(writer, form.getId());
		writer.key("form");
		writer.value(form.getClass().getSimpleName());
		writer.key("name");
		writer.value(form.getName().getValue());
		writer.key("type");
		writer.value(form.getType().name());
		writer.endObject();
	}
}
