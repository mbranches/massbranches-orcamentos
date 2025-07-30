import { Eye, LucideSquarePen, LucideTrash2 } from 'lucide-react';
import { formatCurrency } from '../utils/format'
import CardAction from './CardAction';

function ItemCard({ item, onViewButtonClick, onEditButtonClick, onDeleteButtonClick }) {
    return (
        <div 
            className="p-4 border border-gray-200 rounded-lg flex flex-col gap-3 cursor-pointer hover:shadow-md"
            onClick={onViewButtonClick}
        >
            <div>
                <h3 className="font-semibold text-lg">
                    {item.name}
                </h3>
            </div>

            <div className="flex flex-col gap-2 justify-between text-sm md:flex-row">
                <div className="flex flex-col text-gray-600">
                    <span>Unidade de medida:</span>
                    
                    <span className="font-bold">
                        {item.unitMeasurement}
                    </span>
                </div>

                <div className="flex flex-col text-gray-600">
                    <span>Preço unitário:</span>
                    
                    <span className="font-bold">
                        {formatCurrency(item.unitPrice)}
                    </span>
                </div>

                <div className="flex flex-col text-gray-600">
                    <span>Quantidade de uso:</span>
                    
                    <span className="font-bold md:text-center">
                        {item.numberOfUses} 
                    </span>
                </div>

                <div className="flex gap-2 h-1/3" onClick={e => e.stopPropagation()}>
                        <CardAction icon={<Eye size={16}/>} onClick={onViewButtonClick}>
                            Ver
                        </CardAction>

                        <CardAction icon={<LucideSquarePen size={16}/>} onClick={onEditButtonClick}>
                            Editar dados
                        </CardAction>

                        <CardAction icon={<LucideTrash2 size={16}/>} onClick={onDeleteButtonClick} color="red">
                            Excluir
                        </CardAction>
                </div>
            </div>
        </div>
    );
}

export default ItemCard;