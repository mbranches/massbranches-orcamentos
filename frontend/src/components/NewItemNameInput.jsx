import { useEffect, useState } from "react";
import ElementDataInput from "./ElementDataInput";
import { searchItemsByName } from "../services/item";
import { toast } from "react-toastify";
import statusValidate from "../utils/statusValidate";

function NewItemNameInput({ placeholder, watch, throwableError, setValue }) {
    const [suggestions, setSuggestions] = useState([]);
    const [showSuggestions, setShowSuggestions] = useState(false);
    const nameValue = watch("name");

    useEffect(() => {
        const fetchSuggestions = async () => {
            if (!nameValue || !nameValue.trim()) {
                setSuggestions([]);
                return;
            }

            try {
                const response = await searchItemsByName(nameValue);
                setSuggestions(response.data);
            } catch (error) {
                const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente");
                statusValidate(status);
            }
        };

        const debounceTimer = setTimeout(() => {
            fetchSuggestions();
        }, 300);

        return () => clearTimeout(debounceTimer);

    }, [nameValue]);

    const selectItem = (item) => {
        setValue("name", item.name, { shouldValidate: true });
        setValue("itemId", item.id, { shouldDirty: true });

        const currentPrice = watch("unitPrice");
        if (!currentPrice || currentPrice === "0" || currentPrice === "") {
            setValue("unitPrice", item.unitPrice, { shouldDirty: true });
        }

        const currentUnitMeasurement = watch("unitMeasurement");
        if (!currentUnitMeasurement || currentUnitMeasurement === "") {
            setValue("unitMeasurement", item.unitMeasurement, { shouldDirty: true });
        }
        
        setSuggestions([]);
        setShowSuggestions(false);
    };


    const handleOnChange = (e) => {
        const newName = e.target.value;
        setValue("name", newName, { shouldValidate: true });

        if(watch("itemId")) {
            setValue("itemId", "", { shouldDirty: true });
            setValue("unitPrice", "", { shouldDirty: true });
            setValue("unitMeasurement", "", { shouldDirty: true });
        }
    };


    return (
        <div className="relative">
            <ElementDataInput 
                value={nameValue || ""}
                onChange={handleOnChange} 
                placeholder={placeholder} 
                throwableError={throwableError}
                onFocus={() => setShowSuggestions(true)} 
                onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
            />

            {showSuggestions && suggestions.length > 0 && (
                <ul className="w-full absolute bottom-full h-[60px] overflow-auto bg-white border border-slate-200 z-10">
                    {suggestions.map(suggestion => 
                        <li 
                            key={suggestion.id} 
                            className="p-2 hover:bg-slate-200 cursor-pointer" 
                            onMouseDown={() => selectItem(suggestion)}
                        >
                            {suggestion.name}
                        </li>
                    )}
                </ul>
            )}
        </div>
    );
}

export default NewItemNameInput;
