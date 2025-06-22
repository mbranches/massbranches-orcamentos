import { Controller } from "react-hook-form";
import FormLabel from "./FormLabel";
import Select from "react-select";

function FormSelect({ id, name, label, control, options }) {
    const mappedOptions = options.map((opt) => ({
        value: opt.id,
        label: opt.name
    }));

    const customStyles = {
        control: (base, state) => ({
          ...base,
          boxShadow: "none",
          borderColor: state.isFocused ? "#90a1b9" : "#cad5e2",
          "&:hover": {
            borderColor: "#cad5e2",
          },
          cursor: "pointer"
        })
      };
      

    return (
        <div>
            <div className="mb-2">
                <FormLabel idToBeReferenced={id}>
                    {label}
                </FormLabel>
            </div>
            
            <Controller 
                name={name}
                control={control}
                render={({ field }) => (
                    <Select 
                        {...field}
                        inputId={id}
                        options={mappedOptions}
                        placeholder="Opcional"
                        isSearchable
                        styles={customStyles}
                        noOptionsMessage={() => "Nenhum cliente encontrado"}
                    />
                )}  
            />
        </div>
    );
}

export default FormSelect;