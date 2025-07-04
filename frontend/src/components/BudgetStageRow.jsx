import BudgetElementActions from "./BudgetElementActions";
import BudgetStage from "./BudgetStage";
import StageData from "./StageData";

function BudgetStageRow({ stage, onDeleteButtonClick }) {
    return (
        <BudgetStage>
            <StageData>{stage?.order}</StageData>
            <StageData>{stage?.name}</StageData>
            <td colSpan={4} />
            <StageData>
                <BudgetElementActions onDeleteButtonClick={onDeleteButtonClick} />
            </StageData>
        </BudgetStage>
    );
}

export default BudgetStageRow;