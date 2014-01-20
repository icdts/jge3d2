#version 330

struct Light {
        vec4 position;
        vec4 ambient;
        vec4 diffuse;
        vec4 specular;
        float constant_attenuation;
    float linear_attenuation;
    float quadratic_attenuation;
    vec3 spot_direction;
    float spot_cutoff;
    float spot_exponent;
    int light_index;
    int num_lights;
};

uniform Lights {
        Light light[2];
} lights;

uniform Material {
        vec4 ambient;
        vec4 diffuse;
        vec4 specular;
        float shininess;
        float alpha;
} material;

uniform sampler2D texture_data;

smooth in vec3 vertex_mod;
smooth in vec3 normal_mod;
in vec2 tex_coord;
out vec4 frag_color;

vec4 phongSpotPass(int light_id) {                
    float nDotVP;       // normal * light direction
    float nDotR;        // normal * light reflection vector
    float pf;           // power factor
    float attenuation;  // computed attenuation factor
    float d;            // distance from surface to light position
    vec3 VP;            // direction from surface to light position
    vec3 reflection;    // direction of maximum highlights
    vec4 frag;                        // sum of all frag operations
    float spotDot;
    float spot_att;

    // Compute vector from surface to light position
    VP = vec3(lights.light[light_id].position) - vertex_mod;

    // Compute distance between surface and light position
    d = length(VP);

    // Normalize the vector from surface to light position
    VP = normalize(VP);

    // Compute attenuation
    attenuation = 1.0f / (lights.light[light_id].constant_attenuation +
                          lights.light[light_id].linear_attenuation * d +
                          lights.light[light_id].quadratic_attenuation * d * d);

        // See if point on surface is inside cone of illumination
        spotDot = dot (-VP, normalize (lights.light[light_id].spot_direction));
        
    if (spotDot < lights.light[light_id].spot_cutoff)
        spot_att = 0.0f;
    else
        spot_att = pow(spotDot, lights.light[light_id].spot_exponent);

        // Combine the spot and distance attenuation
    attenuation *= spot_att;

    reflection = normalize(reflect(-normalize(VP), normalize(normal_mod)));

    nDotVP = max (0.0f, dot (normal_mod, VP));
    nDotR = max (0.0f, dot(normalize(normal_mod), reflection));

    if (nDotVP == 0.0f) {
        pf = 0.0f;
    } else {
                pf = pow(nDotR, material.shininess);
    }

    vec4 ambient = material.ambient * lights.light[light_id].ambient * attenuation;
    vec4 diffuse = material.diffuse * lights.light[light_id].diffuse * nDotVP * attenuation;
    vec4 specular = material.specular * lights.light[light_id].specular * pf * attenuation;
        
          frag = ambient + diffuse + specular;
          frag.a = material.alpha;
          
          return frag;
}

vec4 phongPointPass(int light_id) {                
    float nDotVP;       // normal * light direction
    float nDotR;        // normal * light reflection vector
    float pf;           // power factor
    float attenuation;  // computed attenuation factor
    float d;            // distance from surface to light position
    vec3 VP;            // direction from surface to light position
    vec3 reflection;    // direction of maximum highlights
    vec4 frag;                        // sum of all frag operations

    // Compute vector from surface to light position
    VP = vec3(lights.light[light_id].position) - vertex_mod;

    // Compute distance between surface and light position
    d = length(VP);

    // Normalize the vector from surface to light position
    VP = normalize(VP);

    // Compute attenuation
    attenuation = 1.0f / (lights.light[light_id].constant_attenuation +
                          lights.light[light_id].linear_attenuation * d +
                          lights.light[light_id].quadratic_attenuation * d * d);

    reflection = normalize(reflect(-normalize(VP), normalize(normal_mod)));

    nDotVP = max (0.0f, dot (normal_mod, VP));
    nDotR = max (0.0f, dot(normalize(normal_mod), reflection));

    if (nDotVP == 0.0f) {
        pf = 0.0f;
    } else {
                pf = pow(nDotR, material.shininess);
    }

    vec4 ambient = material.ambient * lights.light[light_id].ambient * attenuation;
    vec4 diffuse = material.diffuse * lights.light[light_id].diffuse * nDotVP * attenuation;
    vec4 specular = material.specular * lights.light[light_id].specular * pf * attenuation;

          frag = ambient + diffuse + specular;
          frag.a = material.alpha;

          return frag;
}

void main(){
        if(material.alpha == 0.0) {
                  discard;
          }
          
          //frag_color = vec4(0,0,0,0);
          frag_color = texture2D(texture_data,tex_coord);
        for(int i=0;i<2;i++) {
                frag_color += phongPointPass(i);
        }
}