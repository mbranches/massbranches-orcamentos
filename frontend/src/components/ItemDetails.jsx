import { X } from "lucide-react";
import { formatCurrency } from "../utils/format";

function ItemDetails({ item, setModalIsOpen }) {
    return (
        <div className="flex flex-col gap-4 rounded-lg">
            <div className="flex justify-between items-center">
                <div>
                    <h4 className="text-xl font-bold">{item.name}</h4>
                </div>

                <div 
                    className="hover:bg-gray-100 p-1 rounded-md cursor-pointer"
                    onClick={() => setModalIsOpen(false)}
                >
                    <X size={18}/>
                </div>
            </div>

            <div className="grid grid-cols-2 gap-10 justify-between text-sm">
                <div className="flex flex-col">
                    <span className="text-gray-600">Unidade de medida:</span>
                
                    <span className="font-bold">
                        {item.unitMeasurement}
                    </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">Preço unitário:</span>
                
                    <span className="font-bold">
                        {formatCurrency(item.unitPrice)}
                    </span>
                </div>

                <div className="flex flex-col">
                    <span className="text-gray-600">Quantidade de usos:</span>
                
                    <span className="font-bold">
                        {item.numberOfUses}
                   </span>
                </div>
            </div>
        </div>
    )
}

export default ItemDetails;