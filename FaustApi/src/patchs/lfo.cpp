#include "../patch.hpp"
#include "lfo.hpp"

std::string faust::Lfo::GetCode()
{
    return "dummy";
}

faust::Patch* faust::Lfo::Create()
{
    return new Lfo;
}

int faust::Lfo::GetNumInputs()
{
    return 0;
}

int faust::Lfo::GetNumOutputs()
{
    return 1;
}

int faust::Lfo::GetInputRate(int channel)
{
    int rate;
    switch (channel) {
        default: {
            rate = -1;
            break;
        }

    }
    return rate;
}

int faust::Lfo::GetOutputRate(int channel)
{
    int rate;
    switch (channel) {
        case 0: {
            rate = 1;
            break;
        }
        default: {
            rate = -1;
            break;
        }
    }
    return rate;
}

void faust::Lfo::ClassInit(int samplingFreq)
{
    //Copiar ClassInit
}

void faust::Lfo::InstanceConstants(int samplingFreq)
{
    fSamplingFreq = samplingFreq;
    fConst0 = (1.0f / std::min<float>(192000.0f, std::max<float>(1.0f, float(fSamplingFreq))));

}

void faust::Lfo::InstanceResetUserInterface()
{
    fHslider0 = FAUSTFLOAT(0.5f);
    fCheckbox0 = FAUSTFLOAT(0.0f);
    fVslider0 = FAUSTFLOAT(0.0f);
    fHslider1 = FAUSTFLOAT(440.0f);
}

void faust::Lfo::InstanceClear()
{
    //Copiar InstanceClear
}

std::pair<std::string, float*>* faust::Lfo::GetParameterList(int* num_parameters)
{
    *num_parameters = 0; //Indicar cantidad de parametros
    std::pair<std::string, float*>* parameter_list = new std::pair<std::string, float*>[*num_parameters];
    std::pair<std::string, float*> parameter;
    //Incluir los parametros de buildUserInterface de la siguiente forma:
    //parameter.first = "freq";
    //parameter.second = &fHslider1;
    //parameter_list[0] = parameter;
    //parameter.first = "gain";
    //parameter.second = &fHslider0;
    //parameter_list[1] = parameter;
    //parameter.first = "gate";
    //parameter.second = &fCheckbox0;
    //parameter_list[2] = parameter;
    return parameter_list;
}

void faust::Lfo::Compute(int count)
{
    //Copiar Compute
    //Asignar los inputs y outputs desde inputs_ y outputs_ al inicio de la funcion
    //EJ: FAUSTFLOAT* output0 = outputs_[0];
    //Reemplazar mydsp_faustpowerX_f por FaustPowerX_f
}
