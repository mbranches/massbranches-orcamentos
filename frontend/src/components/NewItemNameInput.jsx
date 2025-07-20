import { useEffect, useState } from "react";
import ElementDataInput from "./ElementDataInput";
import { useAuth } from "../hooks/useAuth";
import { searchItemsByName } from "../services/item";
import { toast } from "react-toastify";

function NewItemNameInput({ value, error, placeholder, newItem, setNewItem, throwableError }) {
    const [query, setQuery] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const [showSuggestions, setShowSuggestions] = useState(false);
    const { isAdmin } = useAuth();

    useEffect(() => {
        const fetchSuggestions = async () => { 
            if(!query.trim()) {
                setSuggestions([]);

                return;
            }

            try {
                const response = isAdmin ? await searchItemsByName(query, true) : await searchItemsByName(query) ;

                setSuggestions(response.data);
            } catch (error) {
                const status = error?.response?.status || toast.error("Ocorreu um erro interno, por favor tente novamente"); 
                
                statusValidate(status);
            }
        }    

        fetchSuggestions();
    }, [query]);

    const selectItem = (item) => {
        setNewItem({
            ...newItem, 
            item: item, 
            unitPrice: !newItem.unitPrice ? item.unitPrice 
                : newItem.unitPrice
        });
    };
    
    return (
        <div className="relative">
            <ElementDataInput 
                value={value} 
                onChange={(e) => {
                    setQuery(e.target.value);
                    setNewItem({...newItem, item: {...newItem.item, id: "", name: e.target.value, unitPrice: ""}});
                }} 
                error={error} 
                placeholder={placeholder} 
                throwableError={throwableError}
                onFocus={() => setShowSuggestions(true)} 
                onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
            />

            {showSuggestions && suggestions.length > 0 && (
                <ul className="w-full absolute bottom-full h-[60px] overflow-auto bg-white border border-slate-200">
                    {suggestions.map(suggestion => 
                        <li 
                            key={suggestion.id} 
                            className="p-2 hover:bg-slate-200" 
                            onClick={() => selectItem(suggestion)}
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