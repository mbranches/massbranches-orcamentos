import BudgetStage from "./BudgetStage";
import ElementDataInput from "./ElementDataInput";
import NewElementActions from "./NewElementActions";
import StageData from "./StageData";

function NewStage({newStage, setNewStage, handlerSave, handlerRemove, newStageErrors}) {
    return (
        newStage && (
            <BudgetStage>
                <StageData>
                    <ElementDataInput
                        value={newStage.order}
                        onChange={(e) => setNewStage({ ...newStage, order: e.target.value })}
                        error={newStageErrors?.order}
                        throwableError={newStageErrors?.order}
                    />
                </StageData>
                <StageData>
                    <ElementDataInput
                        value={newStage.name}
                        onChange={(e) => setNewStage({ ...newStage, name: e.target.value })}
                        placeholder="Nome da Etapa"
                        throwableError={newStageErrors?.name}
                    />
                </StageData>
                <td colSpan={4}></td>
                <StageData>
                    <NewElementActions handlerSave={handlerSave} handlerRemove={handlerRemove} />
                </StageData>
            </BudgetStage>
        )
    );
}

export default NewStage;